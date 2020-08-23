package main

import (
	"fmt"
	"log"
	"net/http"
	"newcomers/backend"

	_ "github.com/go-sql-driver/mysql"
)

func main() {
	fs := http.FileServer(http.Dir("./frontend/static"))
	http.Handle("/static/", http.StripPrefix("/static/", fs))

	http.HandleFunc("/", backend.Index)
	http.HandleFunc("/noticeboard_find/", backend.NoticeboardFind)
	http.HandleFunc("/noticeboard_insert/", backend.NoticeboardInsert)
	http.HandleFunc("/send_alarm/", backend.SendPushAlarm)

	log.Println("Listening on : 9090...")
	err := http.ListenAndServe(":9090", nil)

	if err != nil {
		log.Fatal("ListenAndServer : ", err)
	} else {
		fmt.Println("ListenAndServer Started! -> Port(9000)")
	}
}
