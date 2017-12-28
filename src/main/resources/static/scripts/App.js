g_App = angular.module('Trace', ['dx']);

g_App.constant('ControlType', {Chart: 0, Result: 1, CellData: 2, Empty: 3} );
g_App.constant('FilterType', [{Id: "All", Desc: "Keine Einschränkung"}, {Id: "SqlTime", Desc: "Laufzeit"}, {Id: "WaitingTime", Desc: "Wartezeit"}]);
g_App.constant('LineType', ["line", "spline", "stepline"]);
g_App.constant('SeriesType', ["Laufzeit", "Wartezeit", "Beide"]);

g_App.value('UserInfo', {UserId: "", 
  						Password: ""});

g_App.value('Selections', {FilterType: {Id: "SqlTime"}, 
						   LineType: {Id: "line"},
						   SeriesType: {Id: "Laufzeit"},
						   SelectedRow: undefined, 
						   ControlType: 0, 
						   FilterTime: 0.1
						});

g_App.value('Statistics', { Filtered: {Count : 0, SumTraceTime : 0, AvgTraceTime : 0, SumWaitingTime : 0},
							  Total: {Count : 0, SumTraceTime : 0, AvtTraceTime : 0, SumWaitingTime : 0, SumTimestampTime : 0}						
						    });
