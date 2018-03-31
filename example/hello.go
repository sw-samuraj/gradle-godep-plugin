package main

import (
	"fmt"
	"github.com/gorilla/mux"
	"net/http"
)

func Hello() string {
	return "Hello, world!"
}

func main() {
	r := mux.NewRouter()
	http.Handle("/", r)
	fmt.Println(Hello())
}
