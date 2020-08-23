<?php
    require_once "DBDetails.php";
    $conn = mysqli_connect(HOST, USER, PASS);

    if(mysqli_connect_errno()){
        echo "접속 실패";
    }
    mysqli_set_charset($conn, "utf8");
    mysqli_select_db($conn, DB);
 
    // 세션 시작
    session_start();


    $AreaText = $_POST['area_text'];
    $TimeText = $_POST['time_text'];
    $SexText = $_POST['sex_text'];
    
    $query = "SELECT * FROM notice_board  WHERE area = '$AreaText' and time_t = '$TimeText' and sex = '$SexText'";
    
    $res = mysqli_query($conn, $query);

    $rows = array();
    $result = array();

    while($row = mysqli_fetch_array($res)){
        $rows["phone_num"] = $row[0];
        $rows["name"] = $row[1];
        $rows["email"] = $row[2];
        $rows["sex"] = $row[3];
        $rows['contents_title'] = $row[4];
        $rows["contents"] = $row[5];
        $rows["area"] = $row[6];
        $rows["time_t"] = $row[7];

        array_push($result, $rows);
    }

    echo json_encode(array("results"=>$result), JSON_UNESCAPED_UNICODE);
    
    mysqli_close($conn);
?>