package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Report;
import com.example.soundvieproject.model.ReportDetail;
import com.example.soundvieproject.model.Song;

import org.bson.types.ObjectId;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class ReportSongActivity extends AppCompatActivity {

    TextView nameUser, nameSong, nameArtistOfSong;
    Helper helper = Helper.INSTANCE;
    Button btn, btnCancel;

    CheckBox cb1, cb2, cb3, cb4,cb5;

    User user;
    Song songReport = null;
    ArrayList<User> arrArtistOfSongReport;
    ArrayList<ArtistInSong> arrArtistInSong;

    com.example.soundvieproject.model.User userCurrent, artistSong;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_song);
        nameUser = findViewById(R.id.nameUser);
        nameSong = findViewById(R.id.nameSong);
        nameArtistOfSong = findViewById(R.id.nameArtist);
        btnCancel = findViewById(R.id.btnCancel);
        btn = findViewById(R.id.btnSendReport);
        cb1 = findViewById(R.id.check1);
        cb2 = findViewById(R.id.check2);
        cb3 = findViewById(R.id.check3);
        cb4 = findViewById(R.id.check4);
        cb5 = findViewById(R.id.check5);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        user = helper.getUser();


        helper.getUserCurrentBy(new App.Callback<com.example.soundvieproject.model.User>() {
            @Override
            public void onResult(App.Result<com.example.soundvieproject.model.User> result) {
                if(result.isSuccess()){
                    userCurrent = result.get();
                    nameUser.setText(userCurrent.getName());

                }
            }
        });



//        String check = "";
//        for(int i = 0; i < testsouce.size(); i++){
//            check += testsouce.get(i);
//        }

        Bundle b = getIntent().getExtras();
        String idSongReport = b.getString("idSongReport");
        //ObjectId idSongReportObj = new ObjectId(idSongReport);
        Log.d("IDSong Report: ", idSongReport + " ");


        helper.getSongByIDPart2(result -> {
            if(result.isSuccess()){
                songReport = result.get();
                nameSong.setText(songReport.getNameSong());

            }
        }, idSongReport);


        helper.getArtitsbyIDSong(result -> {
            if(result.isSuccess()){
                arrArtistOfSongReport = new ArrayList<>();
                MongoCursor<ArtistInSong> cursorArtist = result.get();
                while (cursorArtist.hasNext()){
                    ArtistInSong artistInSong = cursorArtist.next();
                    arrArtistInSong.add(new ArtistInSong(artistInSong.getId(), artistInSong.getIdUser(), artistInSong.getIdSong()));

                    String  IDArtistCurrent = arrArtistInSong.get(0).getIdUser();

                    helper.getArtistOfSongbyID(element -> {
                        if(element.isSuccess()){
                            artistSong = element.get();
                            nameArtistOfSong.setText(artistSong.getName());
                        }
                    }, IDArtistCurrent);
                }
            }
        },idSongReport);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] arrLyDo = new String[]{"Nội dung khiêu dâm", "Tính chất bạo lực hoặc nguy hiểm", "Kích động thù địch, hoặc lạm dụng", "Nội dung lừa đảo", "Phân biệt chủng tộc"};
                ArrayList<CheckBox> checkBoxes = new ArrayList<>();
                checkBoxes.add(cb1);
                checkBoxes.add(cb2);
                checkBoxes.add(cb3);
                checkBoxes.add(cb4);
                checkBoxes.add(cb5);
                String resultReason = null;
                ArrayList<String> testsouce = new ArrayList<>();
                for(int i =0; i < checkBoxes.size(); i++){
                    if(checkBoxes.get(i).isChecked()){
                        testsouce.add(arrLyDo[i]);
                    }
                }
                String lydo = "";
                for(int i = 0; i < testsouce.size(); i++){
                    lydo += testsouce.get(i) + ", ";
                }
                Log.d("Check ly do", lydo);

                ObjectId idRp = new ObjectId();

                Report report = new Report(idRp, 0, new ObjectId(idSongReport), 0);
                helper.reportSong(report);

                ReportDetail reportDetail = new ReportDetail(new ObjectId(), lydo, user.getId(), idRp);
                helper.userReport(reportDetail);
            }
        });

    //        helper.getSongByID(result -> {
    //
    //        }, String.valueOf(idSongReportObj));



    }
}