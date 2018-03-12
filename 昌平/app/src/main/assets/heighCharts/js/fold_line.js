function HighchartFoldLine(charsflod) {
	Highcharts.setOptions({
		// 所有语言文字相关配置都设置在 lang 里
		lang: {
			resetZoom: '重置',
			resetZoomTitle: '重置缩放'
		}
	});
	$('#container').highcharts({
		chart: {
			zoomType: 'x',
			type: 'line',
			panning: true,
			panKey: 'shift',
			selectionMarkerFill: 'rgba(0,0,0, 0.2)',
			resetZoomButton: {
				// 按钮定位
				position: {
					align: 'right', // by default
					verticalAlign: 'top', // by default
					x: 0,
					y: -30
				},
				// 按钮样式
				//				theme: {
				//					fill: 'white',
				//					stroke: 'silver',
				//					r: 0,
				//					states: {
				//						hover: {
				//							fill: '#41739D',
				//							style: {
				//								color: 'red'
				//							}
				//						}
				//					}
				//				}
			}
		},
		credits: {
			enabled: false
		},
		exporting: {
			enabled: false
		},
		title: {
			text: charsflod._title,
			x: -20 //center
		},
		subtitle: {
			text: '来源: ' + charsflod._source,
			x: -20
		},
		xAxis: {
			categories: charsflod._times_h
		},
		yAxis: {
			title: {
				text: null
			},
			//			plotLines: [{
			//				value: 0,
			//				width: 1,
			//				color: '#808080'
			//			}]
		},
		//		tooltip: {
		//			valueSuffix: '°C'
		//		},
		//							legend: {
		//								layout: 'vertical',
		//								align: 'right',
		//								verticalAlign: 'middle',
		//								borderWidth: 0
		//							},
		series: charsflod._series
	});
};