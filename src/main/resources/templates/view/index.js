$(document).ready(function() {

    $('#inputUploadFile').on('click', function () {
        var formData = new FormData($('#form')[0]);
        $.ajax({
            type : 'POST',
            url : '/upload',
            data : formData,
            cache : false,
            processData : false,
            contentType : false
        }).done(function() {
            alert("上传成功");
        }).fail(function() {
            alert("上传失败");
        });
    });

});