package repositories

import models.ApiResponseModel
import models.PlaceModel
import services.ApiService
import utils.splitTopLevelObjects

class PlaceRepository {
    fun fetchPlace(): ApiResponseModel<List<PlaceModel>> {
        lateinit var response: ApiResponseModel<List<PlaceModel>>
        try {
            val jsonData = ApiService.get("getplaces")
            response = ApiResponseModel.fromJson(jsonData) { dataMapper ->
                dataMapper.splitTopLevelObjects().map { place ->
                    PlaceModel.fromJson(place)
                }
            }

        } catch (e: Exception) {
            response = ApiResponseModel(
                code = "500",
                status = "error",
                message = "Failed to fetch places",
                messageDev = e.message ?: e.toString(),
                data = emptyList()
            )
            println(response.toString())
        }
        return response
    }
}