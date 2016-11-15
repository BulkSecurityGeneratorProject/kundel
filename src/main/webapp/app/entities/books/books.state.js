(function() {
    'use strict';

    angular
        .module('kundelApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('books', {
            parent: 'entity',
            url: '/books?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Books'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/books/books.html',
                    controller: 'BooksController',
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
        .state('books-detail', {
            parent: 'entity',
            url: '/books/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Books'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/books/books-detail.html',
                    controller: 'BooksDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Books', function($stateParams, Books) {
                    return Books.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'books',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('books-detail.edit', {
            parent: 'books-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/books/books-dialog.html',
                    controller: 'BooksDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Books', function(Books) {
                            return Books.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('books.new', {
            parent: 'books',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/books/books-dialog.html',
                    controller: 'BooksDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                idBook: null,
                                title: null,
                                isbn: null,
                                year: null,
                                url_s: null,
                                url_m: null,
                                url_l: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('books', null, { reload: 'books' });
                }, function() {
                    $state.go('books');
                });
            }]
        })
        .state('books.edit', {
            parent: 'books',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/books/books-dialog.html',
                    controller: 'BooksDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Books', function(Books) {
                            return Books.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('books', null, { reload: 'books' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('books.delete', {
            parent: 'books',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/books/books-delete-dialog.html',
                    controller: 'BooksDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Books', function(Books) {
                            return Books.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('books', null, { reload: 'books' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
