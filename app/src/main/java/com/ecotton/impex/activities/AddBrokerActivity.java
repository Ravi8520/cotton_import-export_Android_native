package com.ecotton.impex.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ecotton.impex.R;
import com.ecotton.impex.adapters.SearchBrokerAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityAddBrokerBinding;
import com.ecotton.impex.models.DistrictModel;
import com.ecotton.impex.models.SearchBrokerModel;
import com.ecotton.impex.models.StateModel;
import com.ecotton.impex.models.StationModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBrokerActivity extends AppCompatActivity {

    ActivityAddBrokerBinding binding;
    private CustomDialog customDialog;
    private SessionUtil mSessionUtil;
    AddBrokerActivity mContext;

    private List<StateModel> stateModelList = new ArrayList<>();
    private List<DistrictModel> districtModelList = new ArrayList<>();
    private List<StationModel> stationModelList = new ArrayList<>();

    private String selectedItem;
    private int selectedState;
    private int selectedDistrict;
    private int selectedStation;
    SearchBrokerAdapter searchBrokerAdapter;
    public static JSONArray jsonArray = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBrokerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();

        binding.scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });

        searchBrokerAdapter = new SearchBrokerAdapter(mContext);
        binding.searchBrokerRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        binding.searchBrokerRecyclerView.setAdapter(searchBrokerAdapter);
        searchBrokerAdapter.setOnItemClickListener(new SearchBrokerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view.getId() == R.id.layout_main) {

                }
            }
        });
        binding.btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendNotification();
            }
        });

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBrokerAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchBrokerAdapter.getFilter().filter(newText);
                return false;
            }
        });
        StateList();
    }

    private void StateList() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("country_id", "1");
            String data = jsonObject.toString();
            Call<ResponseModel<List<StateModel>>> call = APIClient.getInstance().state_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<StateModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<StateModel>>> call, Response<ResponseModel<List<StateModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        setUpSpinerState(response.body().data);
                        //stateAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        //stateAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<StateModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpSpinerState(List<StateModel> list) {
        stateModelList.clear();
        stateModelList.addAll(list);
        StateAdapter adapter = new StateAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, stateModelList);
        binding.spinnerState.setAdapter(adapter);
        binding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchBrokerAdapter.mArrayList.clear();
                selectedState = stateModelList.get(position).getId();
                DistrictList(stateModelList.get(position).getId());
                searchBrokerAdapter.notifyDataSetChanged();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void DistrictList(int id) {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state_id", id);
            String data = jsonObject.toString();
            Call<ResponseModel<List<DistrictModel>>> call = APIClient.getInstance().district_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<DistrictModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<DistrictModel>>> call, Response<ResponseModel<List<DistrictModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        searchBrokerAdapter.mArrayList.clear();
                        setUpSpinerDistrict(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {

                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<DistrictModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpSpinerDistrict(List<DistrictModel> list) {
        districtModelList.clear();
        districtModelList.addAll(list);
        DistrictAdapter adapter = new DistrictAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, districtModelList);
        binding.spinnerDistrict.setAdapter(adapter);
        binding.spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchBrokerAdapter.mArrayList.clear();
                selectedDistrict = districtModelList.get(position).getId();
                searchBrokerAdapter.notifyDataSetChanged();
                //StationList(districtModelList.get(position).getId());
                GetBrokerList();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void StationList(int id) {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("district_id", id);
            String data = jsonObject.toString();
            Call<ResponseModel<List<StationModel>>> call = APIClient.getInstance().station_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<StationModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<StationModel>>> call, Response<ResponseModel<List<StationModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        searchBrokerAdapter.mArrayList.clear();
                        setUpSpinerStation(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<StationModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpSpinerStation(List<StationModel> list) {
        stationModelList.clear();
        stationModelList.addAll(list);
        StationAdapter adapter = new StationAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, stationModelList);
        binding.spinnerStation.setAdapter(adapter);
        binding.spinnerStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStation = stationModelList.get(position).getId();
                searchBrokerAdapter.mArrayList.clear();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class StateAdapter extends ArrayAdapter<StateModel> {

        LayoutInflater flater;

        public StateAdapter(Context context, int resouceId, int textviewId, List<StateModel> list) {

            super(context, resouceId, textviewId, list);
//        flater = context.getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return rowview(convertView, position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView, position);
        }

        private View rowview(View convertView, int position) {

            StateModel rowItem = getItem(position);

            viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getName());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    public class StationAdapter extends ArrayAdapter<StationModel> {

        LayoutInflater flater;

        public StationAdapter(Context context, int resouceId, int textviewId, List<StationModel> list) {
            super(context, resouceId, textviewId, list);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return rowview(convertView, position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView, position);
        }

        private View rowview(View convertView, int position) {

            StationModel rowItem = getItem(position);

            viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getName());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    public class DistrictAdapter extends ArrayAdapter<DistrictModel> {

        LayoutInflater flater;

        public DistrictAdapter(Context context, int resouceId, int textviewId, List<DistrictModel> list) {
            super(context, resouceId, textviewId, list);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return rowview(convertView, position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView, position);
        }

        private View rowview(View convertView, int position) {

            DistrictModel rowItem = getItem(position);

            viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getName());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    private void GetBrokerList() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("country_id", 1);
            jsonObject.put("state_id", selectedState);
            jsonObject.put("district_id", selectedDistrict);
            jsonObject.put("city_id", "");
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            String data = jsonObject.toString();
            Call<ResponseModel<List<SearchBrokerModel>>> call = APIClient.getInstance().search_broker(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<SearchBrokerModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<SearchBrokerModel>>> call, Response<ResponseModel<List<SearchBrokerModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        searchBrokerAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        searchBrokerAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<SearchBrokerModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SendNotification() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("broker_id", jsonArray);
            String data = jsonObject.toString();
            Log.e("SendNotification", "SendNotification==" + data);
            Call<ResponseModel<SearchBrokerModel>> call = APIClient.getInstance().send_broker_request(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<SearchBrokerModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<SearchBrokerModel>> call, Response<ResponseModel<SearchBrokerModel>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(mContext, response.body().message);
                        onBackPressed();
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, response.body().message);
                        onBackPressed();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<SearchBrokerModel>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}