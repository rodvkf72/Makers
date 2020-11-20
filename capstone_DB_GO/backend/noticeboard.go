package backend

import (
	"fmt"
	"net/http"

	"github.com/labstack/echo"
)

func Echo_Noticeboardcheck(c echo.Context) error {
	resphonenum := c.FormValue("chkphonenum")
	query := "SELECT * FROM noticeboard_setting WHERE phone_num = " + "'" + resphonenum + "'" + ";"
	result := CheckQuery(db, query)
	return c.HTML(http.StatusOK, fmt.Sprint(string(result)))
}

func Echo_Noticeboardcontentcheck(c echo.Context) error {
	resphonenum := c.FormValue("cudb_phonenum")
	query := "SELECT * FROM info WHERE phone_num = " + "'" + resphonenum + "'" + ";"
	result := ContentCheckQuery(db, query)
	return c.HTML(http.StatusOK, fmt.Sprint(string(result)))
}

func Echo_Noticeboardcontents(c echo.Context) error {
	rescontent := c.FormValue("u_contents_title")
	query := "SELECT * FROM noticeboard WHERE title = " + "'" + rescontent + "'" + ";"
	result := FindsQuery(db, query)
	return c.HTML(http.StatusOK, fmt.Sprint(string(result)))
}

func Echo_Noticeboarddelete(c echo.Context) error {
	resno := c.FormValue("num")
	query := "DELETE FROM noticeboard WHERE no =" + "'" + resno + "'" + ";"
	DeleteQuery(db, query)
	return c.HTML(http.StatusOK, fmt.Sprint("delete"))
}

func Echo_Noticeboardfind(c echo.Context) error {
	var query string

	resphonenum := c.FormValue("phone_num")
	ressex := c.FormValue("sex_text")
	resarea := c.FormValue("area_text")
	restime := c.FormValue("time_text")

	if (restime == "") || (ressex == "") {
		query = "SELECT * FROM noticeboard WHERE phone_num = " + "'" + resphonenum + "'" + "ORDER BY no DESC;"
	} else {
		query = "SELECT * FROM noticeboard WHERE area = " + "'" + resarea + "'" + "AND sex = " + "'" + ressex + "'" + "ORDER BY no DESC;"
	}
	jsondata := FindsQuery(db, query)
	return c.HTML(http.StatusOK, fmt.Sprint(string(jsondata)))
}

func Echo_Noticeboardinsert(c echo.Context) error {
	var insertstring string

	resphonenum := c.FormValue("enroll_phonenum")
	resname := c.FormValue("enroll_name")
	resemail := c.FormValue("enroll_email")
	ressex := c.FormValue("enroll_sex")
	restitle := c.FormValue("enroll_contents_title")
	resmain := c.FormValue("enroll_contents")
	resarea := c.FormValue("enroll_area")
	restime := c.FormValue("enroll_time")

	if resphonenum == "" || resname == "" || resemail == "" || ressex == "" || restitle == "" || resmain == "" || resarea == "" || restime == "" {
		return c.HTML(http.StatusOK, fmt.Sprint("fail"))
	} else {
		insertstring = "INSERT INTO noticeboard(phone_num, name, email, sex, title, content, area, time_t) VALUES (" + "'" + resphonenum + "'" + "," + "'" + resname + "'" + "," + "'" + resemail + "'" + "," + "'" + ressex + "'" + "," + "'" + restitle + "'" + "," + "'" + resmain + "'" + "," + "'" + resarea + "'" + "," + "'" + restime + "'" + ");"
		InsertQuery(db, insertstring)
		return c.HTML(http.StatusOK, fmt.Sprint("complete"))
		//http.Redirect(w, r, "/send_alarm/", http.StatusFound)
	}
}

/*
 게시판 권한이 있는지 확인
*/
func NoticeboardCheck(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resphonenum := r.FormValue("chkphonenum")

		query := "SELECT * FROM noticeboard_setting WHERE phone_num = " + "'" + resphonenum + "'" + ";"

		result := CheckQuery(db, query)

		fmt.Fprintf(w, string(result))
	} else {
		resphonenum := "1234"
		query := "SELECT * FROM noticeboard_setting WHERE phone_num = " + "'" + resphonenum + "'" + ";"
		result := CheckQuery(db, query)
		fmt.Fprintf(w, string(result))
	}
}

func NoticeboardContentCheck(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resphonenum := r.FormValue("cudb_phonenum")

		query := "SELECT * FROM info WHERE phone_num = " + "'" + resphonenum + "'" + ";"

		result := ContentCheckQuery(db, query)

		fmt.Fprintf(w, string(result))
	}
}

/*
 선택된 게시글 출력
*/
func NoticeboardContents(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	//go lang json 방식(marshal) 참고
	if r.Method == "POST" {
		rescontent := r.FormValue("u_contents_title")

		query := "SELECT * FROM noticeboard WHERE title = " + "'" + rescontent + "'" + ";"

		result := FindsQuery(db, query)

		fmt.Fprintf(w, string(result))
	} else {
		//테스트용 쿼리. 주소에 직접 접속하여 POST가 아니더라도 값을 확인하기 위해
		rescontent := "test title"

		query := "SELECT * FROM noticeboard WHERE title = " + "'" + rescontent + "'" + ";"

		result := FindsQuery(db, query)

		fmt.Fprintf(w, string(result))
	}
}

/*
 게시글 삭제 기능
*/
func NoticeboardDelete(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resno := r.FormValue("num")

		query := "DELETE FROM noticeboard WHERE no =" + "'" + resno + "'" + ";"

		DeleteQuery(db, query)
		fmt.Fprintf(w, "delete")
	}
}

/*
 게시글을 검색
 게시글 삭제의 경우 자신의 게시글만 보이도록 전화번호 값만 가져와서 검색하고
 게시글을 볼 때는 지역, 시간, 성별을 확인
*/
func NoticeboardFind(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	var query string

	//go lang json 방식(marshal) 참고
	if r.Method == "POST" {
		resphonenum := r.FormValue("phone_num")
		ressex := r.FormValue("sex_text")
		resarea := r.FormValue("area_text")
		restime := r.FormValue("time_text")

		if (restime == "") || (ressex == "") {
			query = "SELECT * FROM noticeboard WHERE phone_num = " + "'" + resphonenum + "'" + "ORDER BY no DESC;"
		} else {
			query = "SELECT * FROM noticeboard WHERE area = " + "'" + resarea + "'" + "AND sex = " + "'" + ressex + "'" + "ORDER BY no DESC;"
		}

		jsondata := FindsQuery(db, query)
		fmt.Fprintf(w, string(jsondata))
	} else {
		//테스트용 쿼리. 주소에 직접 접속하여 POST가 아니더라도 값을 확인하기 위해
		query = "SELECT * FROM noticeboard WHERE phone_num=1234;"

		jsondata := FindsQuery(db, query)

		fmt.Fprintf(w, string(jsondata))
	}
}

/*
 작성된 게시글 삽입
*/
func NoticeboardInsert(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()
	if r.Method == "POST" {
		var insertstring string
		resphonenum := r.FormValue("enroll_phonenum")
		resname := r.FormValue("enroll_name")
		resemail := r.FormValue("enroll_email")
		ressex := r.FormValue("enroll_sex")
		restitle := r.FormValue("enroll_contents_title")
		resmain := r.FormValue("enroll_contents")
		resarea := r.FormValue("enroll_area")
		restime := r.FormValue("enroll_time")
		resimg := r.FormValue("enroll_image")

		if resphonenum == "" || resname == "" || resemail == "" || ressex == "" || restitle == "" || resmain == "" || resarea == "" || restime == "" {
			fmt.Fprintf(w, "fail")
		} else {
			insertstring = "INSERT INTO noticeboard(phone_num, name, email, sex, title, content, area, time_t, image) VALUES (" + "'" + resphonenum + "'" + "," + "'" + resname + "'" + "," + "'" + resemail + "'" + "," + "'" + ressex + "'" + "," + "'" + restitle + "'" + "," + "'" + resmain + "'" + "," + "'" + resarea + "'" + "," + "'" + restime + "'" + "," + "'" + resimg + "'" + ");"
			InsertQuery(db, insertstring)
			fmt.Fprintf(w, "complete")
			//http.Redirect(w, r, "/send_alarm/", http.StatusFound)
		}
	} else {
		fmt.Fprintf(w, "fail")
	}
}
