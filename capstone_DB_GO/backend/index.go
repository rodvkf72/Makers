package backend

import (
	"html/template"
	"net/http"
)

func Index(w http.ResponseWriter, r *http.Request) {
	indexTemplate, _ := template.ParseFiles("frontend/index.html")
	indexTemplate.Execute(w, nil)
}
