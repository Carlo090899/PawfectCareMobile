package com.example.pawfectcareapp.API;


import com.example.pawfectcareapp.ui.Dogs.model.DogModel;
import com.example.pawfectcareapp.ui.Dogs.model.DogProfileResponse;
import com.example.pawfectcareapp.ui.FeedChart.model.FoodChartResponse;
import com.example.pawfectcareapp.ui.Login.model.UserResponse;
import com.example.pawfectcareapp.ui.Registration.model.RegistrationModel;
import com.example.pawfectcareapp.ui.Registration.model.RegistrationResponse;
import com.example.pawfectcareapp.ui.Task.model.TaskResponse;
import com.example.pawfectcareapp.ui.Team.model.TeamResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiCall {

    /*AUTHENTICATION API CALLS*/

    @GET("user/user_login")
    Call<UserResponse> getUser(@Query("username") String username, @Query("password") String password, @Query("token") String token);

    @POST("user/user_registration")
    Call<RegistrationResponse> saveRegistration(@Body RegistrationModel model);

    @POST("user/verify-email")
    Call<RegistrationResponse> activateUser(@Query("email") String email, @Query("otpCode") int otpCode);


    /*TASK API CALLS*/

    @GET("task/get_tasks")
    Call<TaskResponse> getTask(@Query("fullname") String fullname, @Query("role_id") int role_id);

    @POST("task/assign_task")
    Call<TaskResponse> assigntask(@Query("task_title") String task_title, @Query("task_desc") String task_desc, @Query("assigned_to") String assignedTo,
                                  @Query("assigned_from") String assignedFrom, @Query("user_id") int userId);

    @GET("task/get_employees")
    Call<UserResponse> getEmployeeList();

    @POST("task/tag_task_as_completed")
    Call<TaskResponse> tagTaskAsCompleted(@Query("task_id") int task_id, @Query("status") String status);

    @POST("task/delete_task")
    Call<TaskResponse> deleteTask(@Query("task_id") int task_id);

    @Multipart
    @POST("dogs/save_dog_details")
    Call<DogProfileResponse> saveDogDetails(@Part List<MultipartBody.Part> file, @Query("dog_name") String dog_name, @Query("birthdate") String birthdate,
                                            @Query("gender") String gender ,@Query("notes") String notes);

    @GET("dogs/get_dog_list")
    Call<DogProfileResponse> getDogList();

    @GET("dogs/get_image_list")
    Call<DogProfileResponse> getAlbumImage(@Query("dog_album_id") int dog_album_id);
    @GET("dogs/get_album")
    Call<DogProfileResponse> getAlbum(@Query("dog_id") int dog_id);

    @POST("dogs/save_album")
    Call<DogProfileResponse> saveAlbumName(@Query("dog_id") int dog_id ,@Query("album_name") String album_name, @Query("user_id") int user_id);

    @Multipart
    @POST("dogs/save_album_image")
    Call<DogProfileResponse> saveAlbumImage(@Part List<MultipartBody.Part> file, @Query("dog_album_id") int dog_album_id, @Query("user_id") int user_id);

    @POST("foods/save_food")
    Call<FoodChartResponse> saveDogFood(@Query("dog_id") int dog_id ,@Query("dog_food") String dog_food, @Query("food_desc") String food_desc, @Query("quantity") String quantity, @Query("user_id") int user_id);

    @GET("foods/get_food")
    Call<FoodChartResponse> getFoods(@Query("dog_id") int dog_id);

    @POST("foods/delete_food")
    Call<FoodChartResponse> deleteFood(@Query("food_id") int food_id);

    @GET("team/get_team")
    Call<TeamResponse> getTeamList(@Query("user_id") int user_id);

    @POST("team/update_status")
    Call<TeamResponse> updateStatus(@Query("id") int id, @Query("is_active") boolean isActive);

}
