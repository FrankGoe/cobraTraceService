l_App = angular.module('Trace');

l_App.controller('CtrAnalysis', function($scope, $timeout, $q, $http, Analysis, Statistics, Selections, VisibleControlType, SeriesTypes) 
{      	
	// Variablen
	$scope.Selections = Selections;
	$scope.VisibleControlType = VisibleControlType;		
	$scope.Analysis = Analysis;		
	$scope.Statistics = Statistics;
	$scope.Selections.VisibleControl = VisibleControlType.Empty;
		
	// Events
	$scope.$watch('Selections.SeriesType.Id', DoInitComponent); 
	$scope.$watch('Analysis.TraceRows', DoInitComponent);
	$scope.$watch('Selections.SelectedRow', DoInitComponent);
	$scope.$watch('Selections.VisibleControl', DoInitComponent); 
	
	InitTab();
	InitChart();
	InitGrid();	
	InitForm();
	
	// Methods

	function DoInitComponent()
	{	
		if (Selections.VisibleControl == VisibleControlType.Chart)
		{
			if ($scope.Selections.SeriesType.Id  == SeriesTypes[0])
			$scope.currentSeries = [{ valueField: 'SqlTime' , name: "Laufzeit"}];
		
			else if  ($scope.Selections.SeriesType.Id  == SeriesTypes[1])
				$scope.currentSeries = [{ valueField: 'WaitingTime', name: "Wartezeit"}];
			else
				$scope.currentSeries = [{ valueField: 'SqlTime' , name: "Laufzeit"}, { valueField: 'WaitingTime', name: "Wartezeit" }];

			$scope.chartData = Analysis.TraceRows;	
		}
						
		if (Selections.VisibleControl == VisibleControlType.Result)
		{		
			$scope.gridData = Analysis.TraceRows;			
			DoSelectGridRow($scope.gridApi, $scope.Selections.SelectedRow);
		}	
		
		if (Selections.VisibleControl == VisibleControlType.CellData)
		{
			if (Selections.SelectedRow != undefined)
			{
				$scope.formData = Selections.SelectedRow;
				
				var l_Elem = document.getElementsByName("SqlText");
			
				if (l_Elem.length > 0)
				{
					l_Elem[0].innerHTML = Selections.SelectedRow.Sql; 
					hljs.highlightBlock(l_Elem[0]);
			
					clipboard.copy(Selections.SelectedRow.Sql);
				}	
			}
		}

		function DoSelectGridRow(p_GridApi, p_Row)
		{
			// prüfen ob Selektion vorhanden ist
			if (p_Row == undefined)
				return;

			// prüfen ob zeile bereits selektiert wurde
			var l_Rows = p_GridApi.getSelectedRowsData();
			if (l_Rows.length > 0 && l_Rows[0] == p_Row) 
				return;

			// Sortierung entfernen sonst klappt das Seitenausrechnen in dieser form nicht
			p_GridApi.clearSorting();				

			var l_PageSize = p_GridApi.pageSize();
			var l_PageIndex = Math.floor(p_Row.Id / l_PageSize);
			var l_Mod = p_Row.Id % l_PageSize;
	
			if (l_Mod == 0)
				l_PageIndex = l_PageIndex -1
						
			// Zeile selektieren
			p_GridApi.selectRows(p_Row);			  
	
			// korrekte Seite auswählen
			if (l_PageIndex !== p_GridApi.pageIndex()) 
				p_GridApi.pageIndex(l_PageIndex);
		}		
	}		
  
	function InitGrid()
	{	
		$scope.gridData = [];
		
		$scope.gridSettings = {bindingOptions: {dataSource: "gridData"},	
						      filterRow: { visible: true },
							  searchPanel: {visible: true},
							  groupPanel: {visible: true},
							  allowColumnResizing: true,
							  columnChooser: {enabled: true},
							  columnFixing: {enabled: true},
							  columnAutoWidth: true,
							  selection: {mode: "single"},
							  paging: {pageSize: 16},
							  columns: [{caption: "Nummer",
										 dataField: "Id",
										 dataType: "number",
										 alignment: "middle"
										}, 
										
										{caption: "Zeitstempel",
										 dataField: "TimeStampStr",
										 dataType: "string",
										 alignment: "middle"										 
										}, 
										
										{caption: "Laufzeit",
										dataField: "SqlTime",
										dataType: "number",
										alignment: "middle"
										}, 
										
										{caption: "Wartezeit",
										dataField: "WaitingTime",
										dataType: "number",										
										alignment: "middle"
										},

										{caption: "Typ",
										dataField: "Typ",
										alignment: "middle",
										dataType: "string"
										},

										{caption: "SQL Statement",
										dataField: "Sql",
										dataType: "string",
										alignment: "left"
										}
									],
									summary: {
										totalItems: [
											{column: 'Id',
											summaryType: 'count'
											},

											{column: 'SqlTime',
											summaryType: 'sum'
											},

											{column: 'SqlTime',
											summaryType: 'avg'
											},
											
											{column: 'WaitingTime',
											summaryType: 'sum'
											},

											{column: 'WaitingTime',
											summaryType: 'avg'
											}
										]
									},
									onInitialized: function (e) {$scope.gridApi = e.component;},
									onCellClick: DoCellClicked,
									onSelectionChanged: DoGridSelectionChanged
								};

		function DoGridSelectionChanged(selectedItems)
		{
			$scope.Selections.SelectedRow = selectedItems.selectedRowsData[0];
		}
	
		function DoCellClicked(p_Cell)
		{
			var component = p_Cell.component;
			
			function initialClick() 
			{
				component.clickCount = 1;
				component.clickKey = p_Cell.key;
				component.clickDate = new Date();
			}
	
			function doubleClick() 
			{
				component.clickCount = 0;
				component.clickKey = 0;
				component.clickDate = null;
	
				$scope.Selections.VisibleControl = VisibleControlType.CellData;
			}
	
			if ((!component.clickCount) || (component.clickCount != 1) || (component.clickKey != p_Cell.key) ) 
			{                
				initialClick();
			}
			
			else if (component.clickKey == p_Cell.key) 
			{
				if (((new Date()) - component.clickDate) <= 300)
					doubleClick();                                
				else
					initialClick();                
			}		
		}
															
	}
		
	function InitForm()
	{
		$scope.formData = Analysis.CreateEmptyItem();

		$scope.frmTraceItem =  {
					colCount: 1,					
					bindingOptions: {
						'formData': 'formData'                
					},

					items: [
							{dataField: "Id",
							  editorOptions: {disabled: true},
							  label: {text: "Nummer"}
					   		}, 

							{dataField: "TimeStampStr",
							 editorOptions: {disabled: true},
							 label: {text: "Zeitstempel"}
							}, 
							
							{dataField: "SqlTimeStr",
							editorOptions: {disabled: true},
							label: {text: "Laufzeit"}
							}, 				
							
							{dataField: "WaitingTimeStr",
							editorOptions: {disabled: true},
							label: {text: "Wartezeit"}
							}, 					
							
							{dataField: "Typ",
							editorOptions: {disabled: true},
							label: {text: "Typ"}							
						    }
						]					
				};			
	}	

	function InitTab()
	{
		$scope.tabSettings = {items: [
							{ text: "Chart"},
							{ text: "Tabelle"},
							{ text: "Datensatz"}
							],
							bindingOptions: {
								selectedIndex: 'Selections.VisibleControl'
							},
							width: 300,
							height: 20,
							selectedIndex: 0
		};
	};		
	
	function InitChart()
	{
		$scope.chartSettings = {title: "",		
								commonSeriesSettings: {argumentField: "ChartArgument", point: { size: 7},
													  tagField : "Id"
							                          },
								bindingOptions: {dataSource: "chartData", 
												"commonSeriesSettings.type": "Selections.LineType.Id",
												series: "currentSeries"
											},		
								argumentAxis: {
									label: {
										format: "HH:mm:ss" //yyyy-MMdd HH:mm:ss
									}
								},								
								useAggregation: false,
								export: {enabled: true},	
								tooltip: {enabled: true,
										customizeTooltip: OnShowTooltip
										},															 						
								legend: {verticalAlignment: "bottom",
										horizontalAlignment: "center",
										itemTextPosition: "bottom"}
										,
								onInitialized: function (e) {
											$scope.chartApi = e.component;    
										},
								onPointClick: DoChartClick
	   };	   

	   function OnShowTooltip(args)
	   {		
		   var l_TraceItem =  Analysis.TraceRows[args.point.tag -1];
		   var l_SeriesValue = 0;
		   
		   if (args.seriesName === "Laufzeit")
			   l_SeriesValue = l_TraceItem.SqlTime
		   else
			   l_SeriesValue = l_TraceItem.WaitingTime;
   
		   return {
				   html: "<div> Nummer: " + l_TraceItem.Id + '  ' +  args.seriesName  + ":  " +l_SeriesValue + "<br>" +  l_TraceItem.Sql.substring(0,200) + "</div>"	
		   }
	   }
   
	   // Methods
	   function DoChartClick(info) 
	   {
		   if (info != undefined)
		   {
			   if (info.target.index <= Analysis.TraceRows.length)
			   {
				   $scope.Selections.SelectedRow = Analysis.TraceRows[info.target.index];	
				   info.target.hideTooltip();
   
				   $scope.Selections.VisibleControl = VisibleControlType.Result;				
			   }
		   }		
	   }	   
	}
});



