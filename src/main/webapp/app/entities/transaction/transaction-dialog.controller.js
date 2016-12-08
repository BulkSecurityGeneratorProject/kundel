(function() {
    'use strict';

    angular
        .module('kundelApp')
        .controller('TransactionDialogController', TransactionDialogController);

    TransactionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Transaction', '$http'];

    function TransactionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Transaction, $http) {
        var vm = this;

        vm.transaction = entity;

        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.account = null;
        fetchData();


        function fetchData(){
            $http.get('api/account/').success(function(accountData) {
                vm.account = accountData;
                vm.transaction.idUser = vm.account.login;
            });
            $http.get('api/books/' + vm.transaction.idBook).success(function(bookData) {
                vm.transaction.book = bookData;
            });
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();

        });

        $scope.evaluatePrice = function (){
            vm.transaction.price = days_between(vm.transaction.dateEnd, vm.transaction.dateStart) + 1;
        }

        function days_between(date1, date2) {

            // The number of milliseconds in one day
            var ONE_DAY = 1000 * 60 * 60 * 24

            // Convert both dates to milliseconds
            var date1_ms = date1.getTime()
            var date2_ms = date2.getTime()

            // Calculate the difference in milliseconds
            var difference_ms = Math.abs(date1_ms - date2_ms)

            // Convert back to days and return
            return Math.round(difference_ms/ONE_DAY)

        }

        var endDate = new Date();
        var startDate = new Date();
        endDate.setDate(startDate .getDate()+30);
        startDate.setDate(startDate .getDate()+1);

        $scope.dateOptions = {
            minDate: startDate,
            maxDate: endDate
        };

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transaction.id !== null) {
                Transaction.update(vm.transaction, onSaveSuccess, onSaveError);
            } else {
                Transaction.save(vm.transaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kundelApp:transactionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateStart = false;
        vm.datePickerOpenStatus.dateEnd = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
