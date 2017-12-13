function confirmQuit(){
	return confirm("确定退出吗？");
}
function changeOvertimeToStandar(overtime, overtimeRound){
	if(checkIsHourMinute(overtime) == true){
		var s = overtime.split(":");
		var hour = parseInt(s[0]);
		var minute = parseInt(s[1]);
		if(minute >= parseInt(overtimeRound)){
			hour = hour + 1;
		}
		minute = 0;
		return hour + ":" + minute;
	} else{
		alert("加班格式应该为HH:mm");
	}
}
function checkIsHourMinute(str){
	var reg = /^[0-9]{1,2}:[0-9]{1,2}$/;
	if(str == null){
		return true;
	}
	if(str.length == 0){
		return true;
	}
	if(reg.test(str)){
		return true;
	}
	
	return false;
}

function checkIsDouble(str){
	var reg = /^\d+(\.\d+)?$/;
	if(str == null){
		return true;
	}
	if(str.length == 0){
		return true;
	}
	if(reg.test(str)){
		return true;
	}
	return false;
}
function changeHourMinuteToStandar(str){
	if(str == null || str.length == 0){
		return null;
	}
	if(checkIsHourMinute(str) == false){
		alert("格式应该为：HH:mm");
		return null;
	}
	var s = str.split(":");
	var hour = parseInt(s[0]) < 10 ? ("0" + parseInt(s[0])) : parseInt(s[0]) + "";
	var minute = parseInt(s[1]) < 10 ? ("0" + parseInt(s[1])) : parseInt(s[1]) + "";
	return hour + ":" + minute;
}
function changeHourMinuteToMinute(str, defaultValue){
	if(str == null || str.length == 0){
		return defaultValue;
	}
	if(checkIsHourMinute(str) == true){
		var s = str.split(":");
		return parseInt(s[0] * 60) + parseInt(s[1]);
	}
}
function checkIsInt(str){
	var reg = /^\d+$/;
	if(str == null){
		return true;
	}
	if(str.length == 0){
		return true;
	}
	if(reg.test(str)){
		return true;
	}
	return false;
}
function changeMinuteToHourMinute(number){
	if(checkIsInt(number) == true){
		var num = parseInt(number);
		var hour = parseInt(num / 60);
		var minute = parseInt(number) - parseInt(hour) * 60;
		return hour + ":" + minute;
	} else{
		return null;
	}
}
