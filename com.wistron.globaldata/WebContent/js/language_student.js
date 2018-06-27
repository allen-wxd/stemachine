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
            	$('#addbtn').text($.i18n.prop('new'));
            	$('#modifybtn').text($.i18n.prop('modify'));
            	$('#delbtn').text($.i18n.prop('del'));
            	$('#importbtn').text($.i18n.prop('import'));
            	$('#exportbtn').text($.i18n.prop('export'));
            	$('#btn_query').text($.i18n.prop('search'));
            	$('#searchTitle').text($.i18n.prop('searchTerm'));
            	$('#match').text($.i18n.prop('courseAssign'));
            	$('.cancle').text($.i18n.prop('close'));
            	$('.save').text($.i18n.prop('save'));
            	$('#sname').attr('placeholder',$.i18n.prop('ssDialog'));
            	$('#importModalLabel').text($.i18n.prop('studentImport'));
            	$('#onlyexcel').text($.i18n.prop('selectedExcel'));
            	$('#templete').text($.i18n.prop('template'));
             }
        });
    });
 //判断是否有浏览器的语言版本，若没有则使用默认值
 function IsInArray(arr,val){  

	　　var testStr=','+arr.join(",")+",";  

	　　return testStr.indexOf(","+val+",")!=-1;  

	}  