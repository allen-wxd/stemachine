$(function(){
	    var languages=['en','en-GB','en-US','zh','zh-CN','zh-TW'] 
	    var language = navigator.language;
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
            	//login.html
            	$('#ltitle').html($.i18n.prop('login'));
                $('#submit').val($.i18n.prop('login'));
                $('#iname').prop("placeholder",$.i18n.prop('user name'));
                $('#ipass').prop("placeholder",$.i18n.prop('password'));
                $('#rlabel').html($.i18n.prop('rememberkey'));
                $('#links a').first().html($.i18n.prop('updatekey'));
                $('#links a').last().html($.i18n.prop('forgot'));
                
                //newpass.html
                $('#ntitle').html($.i18n.prop('updatekey'));
                $('h1 strong').html($.i18n.prop('updatekey'));
                $('#nlabel1').html($.i18n.prop('uname'));
                $('#nlabel2').html($.i18n.prop('upass'));
                $('#nlabel3').html($.i18n.prop('unewpass'));
                $('#nlabel4').html($.i18n.prop('unewpass1'));
                $('#nsubmit').html($.i18n.prop('usubmit'));
                $('#back').html($.i18n.prop('uback'));
                
                //teacherpage.jsp
                $('#ttitle').html($.i18n.prop('ttitle'));
                $('#outa i').html($.i18n.prop('ttip6'));
                $('#helpa i').html($.i18n.prop('ttip7'));
                //$('#teatspan').html($.i18n.prop('tteacher'));
                //$('#namespan').html($.i18n.prop('teaname'));
                $('#tclasslist').html($.i18n.prop('tclasses'));
                $('#tstudentlist').html($.i18n.prop('tstudents'));
                $('#searchinput').prop("placeholder",$.i18n.prop('ttip'));
                $('#searchbutton').html($.i18n.prop('tsearch'));
                $('#sidspan').html($.i18n.prop('ttip8'));
                $('#snamespan').html($.i18n.prop('ttip9'));
                
                
             }
        });
    });
 //判断是否有浏览器的语言版本，若没有则使用默认值
 function IsInArray(arr,val){  

	　　var testStr=','+arr.join(",")+",";  

	　　return testStr.indexOf(","+val+",")!=-1;  

	}  