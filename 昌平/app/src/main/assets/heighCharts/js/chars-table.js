/**
 * 将Highcharts图表数据生成Table表格
 * @param div  统计图表的div的Id
 * @param table  要生成表格的div的Id
 * @param unitName  各个统计图的单位信息
 */
function HighchartsToTable(div, table, unitName) {
	//获取图表对象
	var chart = div.highcharts();
	if(chart != null) {
		//获取X轴集合对象
		var categories = chart.xAxis[0].categories;
		//获取series集合
		var seriesList = chart.series;
		//获取标题
		var title = chart.title.textStr;
		//先清空原表格内容
		table.html("");
		//获取表格div对象
		var tableObj = table;
		//定义行元素<tr></tr>
		var tr;
		//定义表格体<table></table>
		var tab;
		tab = $("<table  class='chars-table' cellspacing='1' cellpadding='1' width='100%'  ></table>")
		tab.appendTo(tableObj);
		//第一行，放置标题
		tr = $("<tr></tr>");
		tr.appendTo(tab);
		var td = $("<td id='title' class='chars-table-title' colspan='" + seriesList.length + 1 + "'  >" + title + "<b class='chars-table-title-unit' >" + unitName + "</b>" + "</td>");
		td.appendTo(tr);
		$("#title").addClass("chars-table-color-title");
		//下一行，放置数据
		tr = $("<tr ></tr>")
		tr.appendTo(tab);
		td = $("<td class='chars-table-title chars-table-color-both'>序号</td>");
		td.appendTo(tr);
		td = $("<td class='chars-table-title chars-table-color-both'>站点</td>");
		td.appendTo(tr);
		//分批插入数据
		for(var i = 0; i < seriesList.length; i++) {
			td = $("<td id='pollute' class='chars-table-title chars-table-color-both'>" + seriesList[i].name + "</td>");
			td.appendTo(tr);
		}
		for(var i = 0; i < categories.length; i++) {
			tr = $("<tr></tr>");
			tr.appendTo(tab);
			if(i % 2 == 0) {
				td = $("<td id='site' class='chars-table-title '>" + i + "</td>");
			} else {
				td = $("<td id='site' class='chars-table-title chars-table-color-both'>" + i + "</td>");
			}
			td.appendTo(tr);
			if(i % 2 == 0) {
				td = $("<td id='site' class='chars-table-title '>" + categories[i] + "</td>");
			} else {
				td = $("<td id='site' class='chars-table-title chars-table-color-both'>" + categories[i] + "</td>");
			}
			td.appendTo(tr);
			//遍历数据点追加数据值
			for(var j = 0; j < seriesList.length; j++) {
				if(i % 2 == 0) {
					td = $("<td id='data' class='chars-table-pollute-data'>" + seriesList[j].data[i].y + "</td>");
				} else {
					td = $("<td id='data' class='chars-table-pollute-data chars-table-color-both'>" + seriesList[j].data[i].y + "</td>");
				}
				td.appendTo(tr);
			}
		}
	} else {
		alert("获取图表对象失败!");
	}
};