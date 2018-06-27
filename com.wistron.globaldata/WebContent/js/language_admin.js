 $(function(){
	    var languages=['en','en-GB','en-US','zh','zh-CN','zh-TW'] 
    	var language=navigator.language;
	    var result= IsInArray(languages,language);
	    if(!result){
	    	language="";
	    }
    	jQuery.i18n.properties({
            name : 'strings', //资源文件名称
            path : '/i18n/', //资源文件路径
            mode : 'map', //用Map的方式使用资源文件中的值
            language :language,
            callback : function() {//加载成功后设置显示内容
            	$('title').html($.i18n.prop('title'));
                $('#studentMenu').text($.i18n.prop('studentMenu'));
                $('#teacherMenu').text($.i18n.prop('teacherMenu'));
                $('#subjectMenu').text($.i18n.prop('subjectMenu'));
                $('#console').text($.i18n.prop('console'));
                $('#welcome').text($.i18n.prop('welcomeLog'));
                $('#role').text($.i18n.prop('administrator'));
                $('#help').text($.i18n.prop('help'));
                $('#exit').text($.i18n.prop('logout'));
             }
        });
    });
 //判断是否有浏览器的语言版本，若没有则使用默认值
 function IsInArray(arr,val){  

	　　var testStr=','+arr.join(",")+",";  

	　　return testStr.indexOf(","+val+",")!=-1;  

	}  