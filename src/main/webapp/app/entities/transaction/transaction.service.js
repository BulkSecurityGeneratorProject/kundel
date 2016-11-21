(function() {
    'use strict';
    angular
        .module('kundelApp')
        .factory('Transaction', Transaction);

    Transaction.$inject = ['$resource', 'DateUtils'];

    function Transaction ($resource, DateUtils) {
        var resourceUrl =  'api/transactions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateStart = DateUtils.convertLocalDateFromServer(data.dateStart);
                        data.dateEnd = DateUtils.convertLocalDateFromServer(data.dateEnd);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateStart = DateUtils.convertLocalDateToServer(copy.dateStart);
                    copy.dateEnd = DateUtils.convertLocalDateToServer(copy.dateEnd);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateStart = DateUtils.convertLocalDateToServer(copy.dateStart);
                    copy.dateEnd = DateUtils.convertLocalDateToServer(copy.dateEnd);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
