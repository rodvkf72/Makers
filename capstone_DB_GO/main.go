package main

import (
	"fmt"
	"log"
	"net/http"
	"Makers/capstone_DB_GO/backend"

	_ "github.com/go-sql-driver/mysql"
)

func main() {
	fs := http.FileServer(http.Dir("./frontend/static"))
	http.Handle("/static/", http.StripPrefix("/static/", fs))

	http.HandleFunc("/", backend.Index)
	http.HandleFunc("/login/", backend.Login)
	http.HandleFunc("/signup/", backend.SingUp)
	http.HandleFunc("/area_setting/", backend.AreaSetting)
	http.HandleFunc("/area_info/", backend.AreaInfo)
	http.HandleFunc("/noticeboard_check/", backend.NoticeboardCheck)
	http.HandleFunc("/noticeboard_find/", backend.NoticeboardFind)
	http.HandleFunc("/noticeboard_insert/", backend.NoticeboardInsert)
	http.HandleFunc("/noticeboard_delete/", backend.NoticeboardDelete)
	http.HandleFunc("/noticeboard_contents/", backend.NoticeboardContents)
	http.HandleFunc("/noticeboard_content_check/", backend.NoticeboardContentCheck)
	http.HandleFunc("/tourpass_check/", backend.TourpassCheck)
	http.HandleFunc("/tourpass_setting/", backend.TourpassSetting)
	http.HandleFunc("/send_alarm/", backend.SendPushAlarm)
	http.HandleFunc("/token_insert/", backend.TokenInsert)

	log.Println("Listening on : 9090...")
	err := http.ListenAndServe(":9090", nil)

	if err != nil {
		log.Fatal("ListenAndServer : ", err)
	} else {
		fmt.Println("ListenAndServer Started! -> Port(9000)")
	}
}
