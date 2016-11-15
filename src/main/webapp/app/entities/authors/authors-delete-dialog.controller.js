(function() {
    'use strict';

    angular
        .module('kundelApp')
        .controller('AuthorsDeleteController',AuthorsDeleteController);

    AuthorsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Authors'];

    function AuthorsDeleteController($uibModalInstance, entity, Authors) {
        var vm = this;

        vm.authors = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Authors.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
