<!DOCTYPE html>
<html>
<head>
	<title>login</title>

	{{> common/head}}
	
	<script>
		function goMain(){
			location.href="/";
		}
		if($.cookie('Todo-X-Auth')){
			goMain();
		}
		$(function(){
			
			$('#loginTypes button').click(function(){
				var type = $(this).data('type');
				
				switch(type){
				case 'google':
					google.googleInit();
					break;
				defalut:
					break;
				}
			});
			
			var google = {
				googleInit : function(){
					var gg = this;
					gapi.load('client:auth2', function() {
						gapi.client.init({
							discoveryDocs: ['https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest','https://www.googleapis.com/discovery/v1/apis/tasks/v1/rest'],
							clientId: '375628126693-5vpf7j7bm35h47qg1jt30ejdbq5ja09b.apps.googleusercontent.com',
							scope: 'https://www.googleapis.com/auth/calendar https://www.googleapis.com/auth/tasks'
						}).then(function () {
							gapi.auth2.getAuthInstance().isSignedIn.listen(gg.updateStatus);
							gg.updateStatus(gapi.auth2.getAuthInstance().isSignedIn.get());
						});
				    });
				},
				updateStatus : function(isSignedIn){
					if(isSignedIn){
						var user = gapi.auth2.getAuthInstance().currentUser.get().getBasicProfile();
						util.postJson('/login',gapi.auth2.getAuthInstance().currentUser.get().getAuthResponse(),function(result){
							if(result.code==1 && result.data.token){
								$.cookie('Todo-X-Auth',result.data.token);
								goMain();
							}else{
								alert(result.message);							
							}
						},function(){
							alert('Unexpected error during login');
						});
			    	}else{
			    		gapi.auth2.getAuthInstance().signIn();
			    	}
				},
				googleSignOut : function(event){
					gapi.auth2.getAuthInstance().signOut();
				}
			}
		});
	</script>
</head>
<body>

	<div>
		<p class="align-center">[ LOGIN ]</p>
	</div>

	<hr />

	<div>
		<ul id="loginTypes">
			<li><button data-type="google">Login by google account</button></li>
		</ul>
	</div>

</body>
</html>