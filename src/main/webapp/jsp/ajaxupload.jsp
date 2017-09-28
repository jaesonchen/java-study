<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="multipart/form-data; charset=utf-8" />
<title>ajax upload页面</title>
<script type="text/javascript" src="../jquery/jquery-1.8.3.min.js"></script>
</head>
<body class="bodybg">

<form id="uploadForm" action="${pageContext.request.contextPath}/upload.do" method="post" enctype="multipart/form-data">
    <input id="file" type="file" name="file"/>
    <!-- <input type ="submit" value="上传"/> -->
    <button id="upload" type="button">upload</button>
</form>


<script type="text/javascript">
	$(document).ready(function(){
		$(function () {
            $("#upload").click(function () {
            	//alert(($("#file").files[0].name));
            	var fileName=$("#file").val();
            	alert(fileName.substring(fileName.lastIndexOf("\\") + 1));
                $.ajax({
                    url: "${pageContext.request.contextPath}/upload.do",
                    type: "POST",
                    cache: false,
                    data: new FormData($("#uploadForm")[0]),
                    contentType: false,
                    processData: false,
                    success: function (data) {
                    	alert(data);
                        /* if (data.status == "200") {
                            alert("上传成功！");
                        }
                        if (data.status == "201") {
                            alert(data.msg);
                        } */
                        //$("#imgWait").hide();
                    },
                    error: function () {
                        alert("上传失败！");
                        //$("#imgWait").hide();
                    }
                });
            });
        });
	});
/* 		$(function () {
            $("#upload").click(function () {
                $("#imgWait").show();
                var formData = new FormData(document.getElementById("tf"));
                //formData.append("file", document.getElementById("file_upload"));   
                $.ajax({
                    url: "${pageContext.request.contextPath}/upload",
                    type: "POST",
                    data: formData,
                    contentType: false,
                    processData: false,
                    success: function (data) {
                        if (data.status == "200") {
                            alert("上传成功！");
                        }
                        if (data.status == "201") {
                            alert(data.msg);
                        }
                        $("#imgWait").hide();
                    },
                    error: function () {
                        alert("上传失败！");
                        $("#imgWait").hide();
                    }
                });
            });
        }); */

</script>
</body>
</html>