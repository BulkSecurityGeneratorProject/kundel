(function() {
    'use strict';

    angular
        .module('kundelApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transaction', {
            parent: 'entity',
            url: '/transaction?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Transactions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transaction/transactions.html',
                    controller: 'TransactionController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('transaction-detail', {
            parent: 'entity',
            url: '/transaction/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Transaction'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transaction/transaction-detail.html',
                    controller: 'TransactionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Transaction', function($stateParams, Transaction) {
                    return Transaction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'transaction',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('transaction-detail.edit', {
            parent: 'transaction-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction/transaction-dialog.html',
                    controller: 'TransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Transaction', function(Transaction) {
                            return Transaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transaction.new', {
            parent: 'transaction',
            url: '{idBook}/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction/transaction-dialog.html',
                    controller: 'TransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                idBook: $stateParams.idBook,
                                idUser: null,
                                dateStart: new Date(),
                                dateEnd: null,
                                price: null,
                                status: true,
                                id: null
                            };
                        }

                    }
                }).result.then(function() {
                    $state.go('transaction', null, { reload: 'transaction' });
                }, function() {
                    $state.go('transaction');
                });
            }]
        })
        .state('transaction.edit', {
            parent: 'transaction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction/transaction-dialog.html',
                    controller: 'TransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Transaction', function(Transaction) {
                            return Transaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transaction', null, { reload: 'transaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transaction.delete', {
            parent: 'transaction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction/transaction-delete-dialog.html',
                    controller: 'TransactionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Transaction', function(Transaction) {
                            return Transaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transaction', null, { reload: 'transaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
