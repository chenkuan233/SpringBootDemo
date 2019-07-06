(function(window, angular, undefined) {'use strict';

	var ngAJAXTabs = angular.module("ngAJAXTabs", []);
	var __paneScope__ = null;
	var __paneScope_enterTabFunc__ = null;

	ngAJAXTabs.directive("tabs", function() {
		return {
			restrict: "E",
			transclude: true,
			scope: {},
			controller: function($scope, $element ,$rootScope) {
				var add = "add";
				var out = "out";
				var enter = "enter";
				var close = "close";
				$scope.selectFlag = 1;//用于判断是否由select方法切换tab 0代表由select切换 1代表由侧边栏addPane切换
				//设置全局变量的值，保存页面scope和方法名称
				$rootScope.setTabPaneScope = function(scope,enterTab) {
					__paneScope__ = scope;
					__paneScope_enterTabFunc__ = enterTab;
				};

				//用于监听页面$rootScope.modelFlag值的变化，实现侧边栏与tab选项卡点击同步
				$rootScope.$watch('modelFlag', function() {
					if($scope.selectFlag == 0){
						$scope.selectFlag = 1;
						return;
					}
					for(var i=0;i<panes.length;i++){
						if($rootScope.modelFlag == panes[i].tabTitle){
							if(panes[i].selected == false){
								$scope.select(panes[i]);
							}

						}
					}
				});

				var panes = $scope.panes = [];
				//选中选项卡
				$scope.select = function(pane) {
					if($rootScope.modelFlag != pane.tabTitle){
						for(var i=0;i<panes.length;i++){
							if($rootScope.modelFlag == panes[i].tabTitle){
								//判断当前页面是否设置了回调方法
								if(panes[i].enterTabFunc != undefined){
									panes[i].paneScope[panes[i].enterTabFunc](panes[i].paneScope,out);
								}
							}
						}
					}
					// 判断当前选项卡是否处于选中状态
					if(pane.selected == false){
						//判断当前页面是否设置了回调方法
						if(pane.enterTabFunc != undefined){
							pane.paneScope[pane.enterTabFunc](pane.paneScope,enter,pane.paneScope.myID);
						}
					}
					if(arguments[1] == 1){
						$scope.selectFlag = 0;//用于判断是否由select方法切换tab 0代表由select切换 1代表由侧边栏addPane切换
					}
					$rootScope.modelFlag = pane.tabTitle;

					angular.forEach(panes, function(pane) {
						pane.selected = false;
					});


					var flag = 0;
					for(var i=0;i<panes.length;i++){
						if(pane.tabTitle == panes[i].tabTitle){
							panes[i].selected = true;
							flag = 1;
						}
					}
					if(flag == 0){
						pane.selected = true;
					}


				};
				//删除选项卡
				$scope.delPane = function(pane){
					if(pane.tabTitle != 'Home'){
						for(var i=0;i<$scope.panes.length;i++){
							if(pane.tabTitle == $scope.panes[i].tabTitle){
								//判断当前页面是否设置了回调方法
								if($scope.panes[i].enterTabFunc != undefined){
									$scope.panes[i].paneScope[$scope.panes[i].enterTabFunc]($scope.panes[i] .paneScope,close);
								}
								$scope.select($scope.panes[i-1]);
								$scope.panes.splice(i,1);
								$rootScope.panes.splice(i,1);
								break;
							}
						}
					}
				};
				//新增选项卡
				this.addPane = function(pane) {
					pane.load();

					if("所辖交易商设置" == pane.tabTitle){
						for(var i=0;i<panes.length;i++){
							if("所辖交易商设置" == panes[i].tabTitle){
								$scope.panes.splice(i,1);
							}
						}

					} 
					else if("可交易商品设置" == pane.tabTitle){
						for(var i=0;i<panes.length;i++){
							if("可交易商品设置" == panes[i].tabTitle){
								$scope.panes.splice(i,1);
							}
						}

					}
					else if("减持参数设置" == pane.tabTitle){
						for(var i=0;i<panes.length;i++){
							if("减持参数设置" == panes[i].tabTitle){
								$scope.panes.splice(i,1);
							}
						}

					}
					panes.push(pane);
					//判断当前页面是否设置了回调方法
					if(pane.enterTabFunc != undefined){
						pane.paneScope[pane.enterTabFunc](pane.paneScope,add);
					}
					$scope.select(pane);

				};

				var moveX = document.getElementById("move_li");
				$scope.moveRight = function(){
					if(moveX.scrollLeft<=(16000-1200))
						moveX.scrollLeft+=135;
				};
				$scope.moveLeft = function(){
					if(moveX.scrollLeft>=0)
						moveX.scrollLeft-=135;
				};
				$scope.closeAll = function(){
					while($scope.panes.length>1){
						$scope.delPane($scope.panes[$scope.panes.length-1]);
					}
				}
			},
			template:
			'<div class="tabbable">' +
			'<ul class="nav nav-tabs"><li class="showLi" id="move_li" style="width:94%;overflow: hidden">'+
			'<ul class="nav nav-tabs" style="width: 16000px;">' +
			'<li ng-repeat="pane in panes" class="showLi" ng-class="{active:pane.selected}">'+
			'<span class="spanTab" ng-click="select(pane,1)"><a href="">{{pane.tabTitle}}</a>&nbsp;<span class="closeButton" ng-click="delPane(pane,$event)"></span></span>' +
			//'<a href="" ng-click="select(pane)">{{pane.tabTitle}}&nbsp;<img src="../framework/ui-ace/assets/img/clear.png" ng-click="delPane(pane,$event)"/></a>' +
			'</li>' +'</ul></li>' +
			'<div style="float: right;margin: 5px 10px 0 0;"><li class="tab-before" id="li-before" ng-click="moveLeft()"> < '+'</li>'+
			'<li class="tab-after" id="li-after" ng-click="moveRight()"> > '+'</li>' +
			'<li class="closeAll" id="closeAll" ng-click="closeAll()">'+'<a href="#">&times;</a></li>' +
			'</div>'+
			'</ul>' +
			//'<div style="float: right;margin: -25px 5px -150px 0;">'+
			//'<span ng-click="moveLeft()" style="margin-right: 8px;cursor:pointer;"> << </span>'+
			//'<span ng-click="moveRight()" style="cursor:pointer;"> >> </span></div>'+
			'<div class="tab-content" ng-transclude></div>' +
			'</div>',
			replace: true
		};
	});

	ngAJAXTabs.directive("pane", ["$http", "$templateCache", "$controller", "$compile","$rootScope", function($http, $templateCache, $controller, $compile,$rootScope) {
		return {
			require: "^tabs",
			restrict: "E",
			transclude: true,
			scope: { tabTitle: "@" },
			link: function(scope, element, attrs, tabsCtrl,rootScope) {
				var templateCtrl, templateScope;

				if (attrs.template && attrs.controller) {
					scope.load = function() {
						$http.get(attrs.template, {cache: $templateCache})
							.then(function(response) {
								templateScope = scope.$new();
								templateScope.isTabbedPane = true;
								//templateCtrl = $controller(attrs.controller, {$scope: templateScope});
								element.html(response.data);
								element.children().data('$ngControllerController', templateCtrl);
								$compile(element.contents())(templateScope);
								scope.paneScope =  __paneScope__;
								scope.enterTabFunc = __paneScope_enterTabFunc__;
								__paneScope__ = null;
								__paneScope_enterTabFunc__ = null;
							});
					};

				}

				tabsCtrl.addPane(scope);
			},
			template:
			'<div class="tab-pane" ng-class="{active: selected}" ng-transclude>' +
			'</div>',
			replace: true
		};
	}]);

})(window, window.angular);