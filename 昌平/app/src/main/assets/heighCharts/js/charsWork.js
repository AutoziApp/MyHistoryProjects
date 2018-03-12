function asynWork(air) {
	var charsflod = new Chars_Work();
	charsflod._title = "空气质量";
	charsflod._source = air.PositionName;
	var times = new Array();
	for(var i = 0; i < 24; i++) {
		times[i] = i + "点";
	}
	charsflod._times_h = times;
	var list = new Array();
	for (var i = 0 ;i < 6;i++) {
		if(list[i] == undefined || list[i] == null) {
			list[i] = new Object();
		}
		for(k in air) {
			if (k == "NO_2" && i == 0) {
				list[i].name = "NO2";
				list[i].data = air[k];
			} else if (k == "S0_2" && i == 1){
				list[i].name = "SO2";
				list[i].data = air.S0_2;
			} else if (k == "PM2.5" && i == 2){
				list[i].name = "PM2.5";
				list[i].data = air["PM2.5"];
			} else if (k == "PM_10" && i == 3){
				list[i].name = "PM10";
				list[i].data = air.PM_10;
			}  else if (k == "CO_1" && i == 4){
				list[i].name = "CO";
				list[i].data = air.CO_1;
			}  else if (k == "O_3" && i == 5){
				list[i].name = "O3";
				list[i].data = air.O_3;
			} 
		}
			for (var j = 0 ;j < list[i].data.length ; j++) {
				if (list[i].data[j] == null || list[i].data[j] == "null") {
					list[i].data[j] = 0;
				}
				list[i].data[j] = parseFloat(list[i].data[j]);
			}
	}
	charsflod._series = list;
//	alert(charsflod._series[0].name);
//	alert(charsflod._series[0].data);
	HighchartFoldLine(charsflod);
	HighchartsToTable($("#container"), $("#table"), (""));
//	postMessage(charsflod, "*");
}
