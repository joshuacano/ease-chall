//Method to insert likes to data store
$(":checkbox").click(function () {
	var title = $(this).data("title");
	var img = $(this).data("img");
	var cus_cd = $(this).data("cus_cd");
	var url = $(this).data("url");
	var subreddit_id = $(this).data("subreddit_id");
	var num_comments = $(this).data("num_comments");
	var formData = {
		"title": title,
		"cus_cd" : cus_cd,
		"url" : url, 
		"img" : img, 
		"num_comments" : num_comments, 
		"subreddit_id" : subreddit_id
	
	};
	$.ajax({
		type: "POST",
		url: "/post-likes",
		data: JSON.stringify(formData),
		contentType: 'application/json', 
		dataType: "json",
		success: function(data){
			console.debug(cus_cd + " saved " + subreddit_id);
		},
		failure: function(errMsg) {
			//TODO: Log information in errorLog
			alert(errMsg);
		}
	});
});
