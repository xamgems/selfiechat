package main

import (
	"fmt"
	"image"
	"image/jpeg"
	"log"
	"net/http"
	"os"
	"path/filepath"
)

type Snap struct {
	img image.Image
}

const (
	hostname     = "0.0.0.0"
	addrFormat   = "%s:%s"
	formImageKey = "snap"
)

func main() {
	if len(os.Args) != 3 {
		log.Fatalf("Usage: ./%s defaultimg port", filepath.Base(os.Args[0]))
	}

	port := os.Args[2]
	retrieve, send := setup(os.Args[1])

	http.HandleFunc("/get_snap", retrieveHandleFunc(retrieve))
	http.HandleFunc("/send_snap", sendHandleFunc(send))

	addr := fmt.Sprintf(addrFormat, hostname, port)
	log.Println("Server is now listening on", addr)
	err := http.ListenAndServe(addr, nil)
	if err != nil {
		log.Fatal("Unexpected server error:", err)
	}
}

func setup(defaultImg string) (retrieve chan Snap, send chan Snap) {
	retrieve, send = make(chan Snap), make(chan Snap)
	r, err := os.Open(defaultImg)
	if err != nil {
		log.Fatal("Couldn't load default image.")
	}

	img, err := jpeg.Decode(r)
	if err != nil {
		log.Fatal("Couldn't decode default image.")
	}

	go func() {
		curr := Snap{img}

		for {
			select {
			case s := <-send:
				curr = s
			case retrieve <- curr:
				log.Println("Sending image")
			}
		}
	}()
	return
}

func retrieveHandleFunc(retrieve chan Snap) func(w http.ResponseWriter, r *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		snap := <-retrieve
		err := jpeg.Encode(w, snap.img, nil)
		if err != nil {
			log.Println("Failed to write image to client.")
			return
		}
		log.Println("Sent image")
	}
}

func sendHandleFunc(send chan Snap) func(w http.ResponseWriter, r *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		log.Println("Client sending new image...")
		mpr, err := r.MultipartReader()
		if err != nil {
			log.Println("Request was not a multipart/form-data POST request.")
			return
		}

		imgPart, err := mpr.NextPart()
		if err != nil {
			log.Println("Image could not be retrieved from multipart request:", err)
			return
		}

		img, err := jpeg.Decode(imgPart)
		if err != nil {
			log.Println("Could not decode image from client POST.")
			return
		}
		send <- Snap{img}
		log.Println("Current snap updated to", imgPart.FileName())
	}
}
