(function() {
    'use strict';

    angular
        .module('kundelApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('authors', {
            parent: 'entity',
            url: '/authors?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Authors'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/authors/authors.html',
                    controller: 'AuthorsController',
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
        .state('authors-detail', {
            parent: 'entity',
            url: '/authors/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Authors'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/authors/authors-detail.html',
                    controller: 'AuthorsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Authors', function($stateParams, Authors) {
                    return Authors.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'authors',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('authors-detail.edit', {
            parent: 'authors-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/authors/authors-dialog.html',
                    controller: 'AuthorsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Authors', function(Authors) {
                            return Authors.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('authors.new', {
            parent: 'authors',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/authors/authors-dialog.html',
                    controller: 'AuthorsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('authors', null, { reload: 'authors' });
                }, function() {
                    $state.go('authors');
                });
            }]
        })
        .state('authors.edit', {
            parent: 'authors',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/authors/authors-dialog.html',
                    controller: 'AuthorsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Authors', function(Authors) {
                            return Authors.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('authors', null, { reload: 'authors' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('authors.delete', {
            parent: 'authors',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/authors/authors-delete-dialog.html',
                    controller: 'AuthorsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Authors', function(Authors) {
                            return Authors.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('authors', null, { reload: 'authors' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
