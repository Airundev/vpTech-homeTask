package com.vp.list.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.vp.list.model.ListItem;
import com.vp.list.model.SearchResponse;
import com.vp.list.service.SearchService;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends ViewModel {
    private final MutableLiveData<SearchResult> liveData = new MutableLiveData<>();
    private final SearchService searchService;

    private String currentTitle = "";
    private final List<ListItem> aggregatedItems = new ArrayList<>();

    @Inject
    ListViewModel(@NonNull SearchService searchService) {
        this.searchService = searchService;
    }

    public LiveData<SearchResult> observeMovies() {
        return liveData;
    }

    public void searchMoviesByTitle(@NonNull String title, int page) {

        if (page == 1 && !title.equals(currentTitle)) {
            aggregatedItems.clear();
            currentTitle = title;
            liveData.setValue(SearchResult.inProgress());
        }
        searchService.search(title, page).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(
                @NonNull Call<SearchResponse> call,
                @NonNull Response<SearchResponse> response
            ) {

                SearchResponse result = response.body();

                if (result != null) {
                    aggregatedItems.addAll(result.getSearch());
                    final SearchResult searchResult = SearchResult.success(
                        aggregatedItems,
                        result.getTotalResults()
                    );
                    liveData.setValue(searchResult);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                liveData.setValue(SearchResult.error());
            }
        });
    }
}
