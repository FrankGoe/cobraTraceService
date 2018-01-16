l_App = angular.module('Trace');

l_App.controller('CtrOptions', function($scope, $http, $q, $timeout, Analysis, Selections, Statistics, FilterType, ControlType, LineType, SeriesType) 
{  
	$scope.Selections = Selections;
	$scope.ControlType = ControlType;		
	$scope.Analysis = Analysis;		
	$scope.FilterType = FilterType;	
	$scope.LineType = LineType;		
	$scope.SeriesType = SeriesType;			
	$scope.Statistics = Statistics;

	$scope.traceServiceActive = false;		
	$scope.traceLoading = true;
	$scope.loadPanelSettings = {closeOnOutsideClick: true,	width: 400, height: 200, bindingOptions: {visible: 'isLoadPanelVisible', message: 'loadingText'}};
	$scope.isLoadPanelVisible = false; 
	$scope.loadingText = "Bitte warten..."			
	
	$scope.onFileNameChanged = doFileNameChanged;	
	$scope.$watch('Selections.FilterType.Id', ExecuteAnalyze); 
	$scope.$watch('Selections.FilterTime', doOnFilterChanged); 			


	$scope.cboLineTypeOptions = {dataSource: LineType,  
			bindingOptions: { value: "Selections.LineType.Id"} 
			};

	$scope.cboSeriesTypeOptions = {dataSource: SeriesType,  
			  bindingOptions: { value: "Selections.SeriesType.Id"} 
			};		

	$scope.cboFilterOptions = getCboFilterOptions();			
    $scope.cboTraceFilesOptions = getCboFileOptions();
    $scope.btnTraceOptions = getBtnTraceOptions();
    $scope.popPathOptions = getPopPathOptions();
    $scope.txtPathOptions = getTxtPathOptions();
    $scope.txtDaysDeleteOffsetOptions = getTxtDaysDeleteOffsetOptions();
    $scope.btnConfirmOptions = getBtnConfirmOptions();		
    $scope.chkDeleteFilesOptions = getChkDeleteFilesOptions();
	$scope.btnReloadFilesOptions = getBtnReloadFilesOptions();	
	
	loadTraceFiles();	

	function loadTraceFiles()
	{
		$http.get("http://localhost:8080/traceFiles/").then(doTraceFilesSuccess, doTraceFilesFailed);	

		function doTraceFilesSuccess(p_Response)
		{
			$scope.traceServiceActive = true;		
			$scope.traceFiles = p_Response.data;	  
			$scope.tracePath = p_Response.data.path;
			$scope.items = p_Response.data.items;  				
			$scope.daysDeleteOffset = p_Response.data.daysDeleteOffset;
			$scope.deleteTrace = false;		  
			$scope.folderPopupVisible = false;               
			$scope.cboFileItems = _.map(p_Response.data.items, "name");
		}
	
		function doTraceFilesFailed(p_Response)
		{
			$scope.traceServiceActive = false;
			$scope.traceFiles  = undefined;
		}		
	}

	function ExecuteAnalyze()
	{	
		if (Analysis.FileName == "")
			return;

		Analysis.TraceRows = [];
		$scope.isLoadPanelVisible = false;
		$scope.isLoadPanelVisible = true;
		$scope.loadingText ="Trace wird analysiert. Bitte warten...";

		// Execute Async
		$timeout(function() {$q.when(Analysis.AnalyzeTrace()).then(doAnalyzeFinished()); }, 50)
	}
	
	function doAnalyzeFinished(p_Result) 
	{
		Selections.ControlType = ControlType.Chart;	
		$scope.isLoadPanelVisible = false;		
		$scope.traceLoading = false;
	};

	function doOnFilterChanged()
	{	
		if (Analysis.FileName == "")
			return;
	
		// Vorherige Verzögere Ausführung abbrechen
		if ($scope.PrevDebounce !== undefined)		
			$scope.PrevDebounce.cancel();

		// Filter Changed verzögert ausführen
		var l_Debounce = _.debounce(DoExecuteFilterChanged, 1000,  {'leading': false, 'trailing': true })	
		$scope.PrevDebounce = l_Debounce;
		l_Debounce();

		function DoExecuteFilterChanged()
		{			
			$scope.loadingText ="Trace wird analysiert. Bitte warten...";			
			$scope.isLoadPanelVisible = true;
			$scope.$apply();
	
			ExecuteAnalyze();
		}
	}	

	function getCboFilterOptions()
	{
		return {dataSource: FilterType,  
				valueExpr: 'Id',
				displayExpr: 'Desc',
				bindingOptions: { value: "Selections.FilterType.Id"}};	
	}
	function getCboFileOptions()
	{
		return {    bindingOptions: {dataSource: "cboFileItems"},
					onValueChanged: doOnSelectBoxChanged,
					itemTemplate: function (p_TraceName) 
					{
						var l_TraceFile = _.filter($scope.items, { 'name': p_TraceName });
						return "<div class='custom-item' title='" + p_TraceName + "'>" + p_TraceName + " | " + l_TraceFile[0].lastModified + " | " + l_TraceFile[0].totalSpaceMb + "</div>";
					}		
		}
	};   

	function getBtnTraceOptions()
	{
		return  {text: String.fromCharCode(9881), 
				onClick: doOnChangeTraceFolder};
	};

	function getPopPathOptions()
	{
		return  {title: 'Einstellungen', 
				bindingOptions: {visible: 'folderPopupVisible'},
				height: "250px",
				width: "800px"};            
	};

	function getTxtPathOptions()
	{
		return  { bindingOptions: { value: "tracePath" },
				width: "500px"};
	};

	function getTxtDaysDeleteOffsetOptions()
	{
		return  { bindingOptions: { value: "daysDeleteOffset" },
				width: "50px"};	
	};

	function getBtnConfirmOptions()
	{
		return {text: "Einstellungen übernehmen", 
				onClick: doChangeTraceOptions};
	};
	
	function getChkDeleteFilesOptions()
	{
		return { bindingOptions: { value: "deleteTrace" },
				width: "50px"};
	};
	
	function getBtnReloadFilesOptions()
	{
		return {text: String.fromCharCode(10226),
				onClick: loadTraceFiles};
	};	

	function doFileNameChanged(p_Element)
	{
		$scope.traceLoading = true;
		$scope.isLoadPanelVisible = true;
		$scope.$apply();

		var l_Reader = new FileReader();

		l_Reader.onload = doOnLoad;		

		if (p_Element.files.length > 0)
		{
			Analysis.FileName = p_Element.files[0].name;
			$scope.loadingText ="Datei '" + Analysis.FileName + "' wird geladen. Bitte warten...";
			$scope.$apply();

			$timeout(function() {l_Reader.readAsText(p_Element.files[0]);}, 500)
			
		}
				
		function doOnLoad() 
		{
			Analysis.TraceFile = l_Reader.result;
			ExecuteAnalyze();
		}			
	}		

	function doChangeTraceOptions()
	{
		$scope.folderPopupVisible = false;

		if ($scope.tracePath != $scope.traceFiles.path)
			changeTracePath();

		if ($scope.deleteTrace)
		{				
			var l_Url = "http://localhost:8080/deleteTraceFiles/";      
			$http.post(l_Url, $scope.daysDeleteOffset).then(pathChangedSuccess);				
		}

		function changeTracePath()
		{
			//var l_Data = {newPath : $scope.tracePath};	
			//$http.post(l_Url, JSON.stringify(l_Data)); //.then(UserResponse);      

			var l_Url = "http://localhost:8080/newTracePath/";      
			$http.post(l_Url, $scope.tracePath).then(pathChangedSuccess, pathChangedFailed);
		}

		function pathChangedSuccess(p_Response)
		{
			$scope.traceFiles = p_Response.data;	  
			$scope.tracePath = p_Response.data.path;
  
			$scope.cboFileItems = _.map(p_Response.data.items, function (p_TraceFile) 
			{
			  return p_TraceFile.name;
			});	  
		}

		function pathChangedFailed(p_Response)
		{
			alert("Fehler: " + p_Response.config.url);
		}
	};

	function doOnChangeTraceFolder()
	{
		$scope.folderPopupVisible = true;
	};

	function doOnSelectBoxChanged(p_Element)
	{
		$scope.traceLoading = true;
		$scope.isLoadPanelVisible = true;
	
		$http.get("http://localhost:8080/traceFile/" + p_Element.value).then(doTraceFileSuccess, doTraceFileFailed);

		function doTraceFileSuccess(p_Response)
		{
			Analysis.FileName = p_Element.value;
			Analysis.TraceFile = p_Response.data;
			
			ExecuteAnalyze();		
		}
		
		function doTraceFileFailed(p_Response)
		{
			alert("Fehler: " + p_Response.config.url)
		}		
	};		
});