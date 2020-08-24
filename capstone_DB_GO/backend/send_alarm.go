package backend

import (
	"context"
	"fmt"
	"log"
	"net/http"

	firebase "firebase.google.com/go"
	"firebase.google.com/go/messaging"
	"google.golang.org/api/option"
)

func SendPushAlarm(w http.ResponseWriter, r *http.Request) {
	//block the GET type.. and send push alarm to topic
	if r.Method == "POST" {
		//var topicselectquery = "SELECT topic FROM info;"
		var areaselectquery = "SELECT area FROM noticeboard ORDER BY no DESC LIMIT 1"
		areaselect := SelectQuery(db, areaselectquery, "area")
		opt := option.WithCredentialsFile("newcomers-521cb-firebase-adminsdk-ggg4x-09b1c5355c.json")
		app, _ := firebase.NewApp(context.Background(), nil, opt)
		ctx := context.Background()
		client, _ := app.Messaging(ctx)
		sendToTopic(ctx, client, areaselect)
	}
	//http.Redirect(w, r, "/", http.StatusFound)
}

func initializeAppDefault() *firebase.App {
	// [START initialize_app_default_golang]
	app, err := firebase.NewApp(context.Background(), nil)
	if err != nil {
		log.Fatalf("error initializing app: %v\n", err)
	}
	// [END initialize_app_default_golang]

	return app
}

func sendToTopic(ctx context.Context, client *messaging.Client, selectedarea string) {
	// [START send_to_topic_golang]
	// The topic name can be optionally prefixed with "/topics/".
	//topic := selectedtopic
	//topic := "newcomers_fcm"

	var topic string

	var title string
	var body string

	if selectedarea == "부산" {
		topic = "busan"
		title = "부산"
		body = "부산"
	} else if selectedarea == "서울" {
		topic = "seoul"
		title = "서울"
		body = "서울"
	} else if selectedarea == "대구" {
		topic = "daegu"
		title = "대구"
		body = "대구"
	}

	/*
		if selectedtopic == "busan" {
			title = "부산"
			body = "부산"
		} else if selectedtopic == "seoul" {
			title = "서울"
			body = "서울"
		} else if selectedtopic == "daegu" {
			title = "대구"
			body = "대구"
		}
	*/

	// See documentation on defining a message payload.
	messages := []*messaging.Message{
		{
			Notification: &messaging.Notification{
				Title: title + " 지역 게시글이 등록 되었습니다.",
				Body:  body + " 지역 게시글이 등록 되었습니다.",
			},
			Data: map[string]string{
				"title": title + " 지역 게시글이 등록 되었습니다.",
				"body":  body + " 지역 게시글이 등록 되었습니다.",
			},
			Topic: topic,
		},
	}

	// Send a message to the devices subscribed to the provided topic.
	br, err := client.SendAll(context.Background(), messages)
	if err != nil {
		log.Fatalln(err)
	}
	// Response is a message ID string.
	fmt.Println("Successfully sent message:", br.SuccessCount)
	// [END send_to_topic_golang]
}

func initializeAppWithRefreshToken() *firebase.App {
	// [START initialize_app_refresh_token_golang]
	opt := option.WithCredentialsFile("path/to/refreshToken.json")
	config := &firebase.Config{ProjectID: "running-plus-9c5ab"}
	app, err := firebase.NewApp(context.Background(), config, opt)
	if err != nil {
		log.Fatalf("error initializing app: %v\n", err)
	}
	// [END initialize_app_refresh_token_golang]

	return app
}
