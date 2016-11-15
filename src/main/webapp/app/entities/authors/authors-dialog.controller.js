(function() {
    'use strict';

    angular
        .module('kundelApp')
        .controller('AuthorsDialogController', AuthorsDialogController);

    AuthorsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Authors'];

    function AuthorsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Authors) {
        var vm = this;

        vm.authors = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.authors.id !== null) {
                Authors.update(vm.authors, onSaveSuccess, onSaveError);
            } else {
                Authors.save(vm.authors, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kundelApp:authorsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
