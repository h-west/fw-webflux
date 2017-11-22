var util = (function($){
	return {
		postJson : function(url,data,cb,err){
			$.ajax({
		        url: url,
		        dataType: 'json',
		        contentType: 'application/json;charset=utf-8',
		        type: 'POST',
		        data: JSON.stringify(data),
		        success: function(data){
		        	if(cb&&typeof cb === "function"){
		        		cb.call(this,data);
		        	}
		        },
		        error : err?err:null
		    });
		},
		
		
		dummy : 'dummy'
	}
})(jQuery);