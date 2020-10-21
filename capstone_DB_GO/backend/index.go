package backend

import (
	"html/template"
	"net/http"
)

/*
 테스트용 첫 페이지
*/
func Index(w http.ResponseWriter, r *http.Request) {
	indexTemplate, _ := template.ParseFiles("frontend/index.html")
	indexTemplate.Execute(w, nil)
}
