package com.example.soundvieproject.DB;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.content.Context;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.soundvieproject.HomeActivity;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Playlist;
import com.example.soundvieproject.model.Premium;
import com.example.soundvieproject.model.Report;
import com.example.soundvieproject.model.ReportDetail;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.SongInPlayList;
import com.example.soundvieproject.model.UserTypes;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.security.KeyStore;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.mongo.result.DeleteResult;
import io.realm.mongodb.mongo.result.UpdateResult;

public class Helper {
    public static final Helper INSTANCE = new Helper();
    private User user;
    private io.realm.mongodb.App a;
    private MongoClient client;
    private MongoDatabase db;
    private CodecRegistry pojoCodecRegistry;

    public Helper() {
        String Appid = "soundvie-zvqhd";
        a = new io.realm.mongodb.App(new AppConfiguration.Builder(Appid).build());
        pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    }

    public User getUser() {
        return a.currentUser();
    }

    public void setUser(User user) {
        this.user = user;
    }


    public io.realm.mongodb.App getA() {
        return a;
    }

    public void setA(io.realm.mongodb.App a) {
        this.a = a;
        user = a.currentUser();
        if (user != null) {
            client = user.getMongoClient("mongodb-atlas");
            db = client.getDatabase("SoundVIE");
        }
        pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    }


    public void getUserCurrentBy(App.Callback<com.example.soundvieproject.model.User> callback){
        user = a.currentUser();
        client = user.getMongoClient("mongodb-atlas");
        db = client.getDatabase("SoundVIE");
        String idUser = Objects.requireNonNull(a.currentUser()).getId();
        Document doc = new Document("idUser", idUser);
        MongoCollection<com.example.soundvieproject.model.User> col = db.getCollection("user", com.example.soundvieproject.model.User.class).withCodecRegistry(pojoCodecRegistry);
        col.findOne(doc).getAsync(callback);
    }

    public void prepareDatabase() {
        MongoCollection<UserTypes> col = db.getCollection("UserTypes", UserTypes.class).withCodecRegistry(pojoCodecRegistry);
        col.insertMany(Arrays.asList(
                new UserTypes("Nghệ sĩ", "Người sáng tạo nội dung cho nền tảng", "ns"),
                new UserTypes("Người dùng", "Người sử dụng ứng dụng", "usr"),
                new UserTypes("Quản lý nhạc", "Quản lý các bài hát vi phạm", "qln"),
                new UserTypes("Quản lý người dùng", "Quản lý phân quyền và tài khoản người dùng", "qlnd"),
                new UserTypes("Kế toán", "Báo cáo các khoản tiền", "kt")
        )).getAsync(task -> {
            if (task.isSuccess()) {
                Log.d("Success!", "successfully inserted userTypes");
            } else {
                Log.d("Failed.", "unsuccessfully inserted userTypes: Error " + task.getError());
            }
        });

    }

    public void insertItem(Song s) {
        MongoCollection<Song> col = db.getCollection("Song", Song.class).withCodecRegistry(pojoCodecRegistry);
        col.insertOne(s).getAsync(task -> {
            if (task.isSuccess()) {
                Log.d("Success", "Added.");
            } else {
                Log.d("Error", "Error: " + task.getError());
            }
        });
    }

    public void login(String email, String password, App.Callback<User> callback) {
        Credentials creds = Credentials.emailPassword(email, password);
        a.loginAsync(creds, callback);
    }

    public void insertPremium(Premium premium) {
        MongoCollection<Premium> pre = db.getCollection("Premium", Premium.class).withCodecRegistry(pojoCodecRegistry);
        pre.insertOne(premium).getAsync(result -> {
            if (result.isSuccess()) {
                Log.d("Success", "Added.");
            } else {
                Log.d("Error", "Error: " + result.getError());
            }
        });
    }
    public void insertArtistInSongWhenUpMusic(ArtistInSong artistInSong){
        MongoCollection<ArtistInSong> artistInSongMongoCollection = db.getCollection("ArtistsInSong", ArtistInSong.class).withCodecRegistry(pojoCodecRegistry);
        artistInSongMongoCollection.insertOne(artistInSong).getAsync(result -> {
            if(result.isSuccess()){
                Log.d("Insert Artists When up music", "Success!");
            }
            else
                Log.d("Insert Artists When up music", "Failed!" + result.getError());

        });
    }

    public void insertSongInPlayList(SongInPlayList songInPlayList){
        MongoCollection<SongInPlayList> song = db.getCollection("SongInPlaylist", SongInPlayList.class).withCodecRegistry(pojoCodecRegistry);
        song.insertOne(songInPlayList).getAsync(result -> {
            if(result.isSuccess()){
                Log.d("Add song in playlist", "Success");
            }
            else
                Log.d("Add song in playlist", "Failed");
        });
    }

    public void reportSong(Report report){
        MongoCollection<Report> rp = db.getCollection("Report", Report.class).withCodecRegistry(pojoCodecRegistry);
        rp.insertOne(report).getAsync(result -> {
            if(result.isSuccess()){
                Log.d("Report", "Success!");
            }
            else
                Log.d("Report", "Failed!");
        });
    }


    public void userReport(ReportDetail detail){
        MongoCollection<ReportDetail> colRPDT = db.getCollection("ReportDetail", ReportDetail.class).withCodecRegistry(pojoCodecRegistry);
        colRPDT.insertOne(detail).getAsync(result -> {
            if(result.isSuccess()){
                Log.d("User Report", "Success!");
            }
            else
                Log.d("User Report", "Failed!");
        });
    }


    public void insertSong(Song song) {
        MongoCollection<Song> songcol = db.getCollection("Song", Song.class).withCodecRegistry(pojoCodecRegistry);
        songcol.insertOne(song).getAsync(result -> {
            if (result.isSuccess()) {
                Log.d("Success", "Added.");
            } else {
                Log.d("Error", "Error: " + result.getError());
            }
        });
    }

    public void addPlaylist(Playlist playl) {
        MongoCollection<Playlist> playlist = db.getCollection("Playlist", Playlist.class).withCodecRegistry(pojoCodecRegistry);
        playlist.insertOne(playl).getAsync(result -> {
            if (result.isSuccess()) {
                Log.d("Insert playlist", "Success");
            } else {
                Log.d("Insert playlist", "Failed because: " + result.getError());
            }
        });
    }


    public void getSong(App.Callback<MongoCursor<Song>> callback) {
        MongoCollection<Song> col = db.getCollection("Song", Song.class).withCodecRegistry(pojoCodecRegistry);
        RealmResultTask<MongoCursor<Song>> task = col.find().iterator();
        task.getAsync(callback);
    }

    public void getSongByID(App.Callback<MongoCursor<Song>> callback, String idSong){
        Document document = new Document("_id", new ObjectId(idSong));
        MongoCollection<Song> songMongoCollection = db.getCollection("Song", Song.class).withCodecRegistry(pojoCodecRegistry);
        RealmResultTask<MongoCursor<Song>> task = songMongoCollection.find(document).iterator();
        task.getAsync(callback);
    }

    public void getSongByIDPart2(App.Callback<Song> callback, String idSong){
        Document document = new Document("_id", new ObjectId(idSong));
        MongoCollection<Song> songMongoCollection = db.getCollection("Song", Song.class).withCodecRegistry(pojoCodecRegistry);
        songMongoCollection.findOne(document).getAsync(callback);

    }
    public void getArtitsbyIDSong(App.Callback<MongoCursor<ArtistInSong>> callback, String idSongRp){
        Document docu = new Document("idSong", idSongRp);
        MongoCollection<ArtistInSong> colArtistInSong = db.getCollection("ArtistInSong", ArtistInSong.class).withCodecRegistry(pojoCodecRegistry);
        RealmResultTask<MongoCursor<ArtistInSong>> task = colArtistInSong.find(docu).iterator();
        task.getAsync(callback);
    }
    public void getArtistOfSongbyID(App.Callback<com.example.soundvieproject.model.User> callback, String idArtist){
        Document docu = new Document("idUser", idArtist);
        MongoCollection<com.example.soundvieproject.model.User> colUser = db.getCollection("user", com.example.soundvieproject.model.User.class).withCodecRegistry(pojoCodecRegistry);
        colUser.findOne(docu).getAsync(callback);
    }



    public void deleteSongWidthID(App.Callback<DeleteResult> callback, ObjectId idSongdel){
        Document docu = new Document("_id", idSongdel);
        MongoCollection<SongInPlayList> songColDel = db.getCollection("SongInPlayList", SongInPlayList.class).withCodecRegistry(pojoCodecRegistry);
        songColDel.deleteOne(docu).getAsync(callback);
    }

    public void getPlayList(App.Callback<MongoCursor<Playlist>> callback) {
        String idUser = a.currentUser().getId().toString();
        Document doc = new Document("idUser", idUser);
        MongoCollection<Playlist> playlist = db.getCollection("Playlist", Playlist.class).withCodecRegistry(pojoCodecRegistry);
        RealmResultTask<MongoCursor<Playlist>> task = playlist.find(doc).iterator();
        task.getAsync(callback);
    }

    public void register(String email, String password, String phone, String name, Context c) {
        a.getEmailPassword().registerUserAsync(email, password, t -> {
            if (t.isSuccess()) {
                Toast.makeText(c, "Đăng ký thành công.", Toast.LENGTH_SHORT).show();
                updateUser(email, password, phone, name, c);
            } else {
                Toast.makeText(c, "Lỗi bất định, vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("Error", "Register error: " + t.getError().getErrorCode().toString());
            }
        });
    }

    public void prepare() {

    }




    public void updateUser(String email, String password, String phone, String name, Context c) {
        //prepare();
        Credentials creds = Credentials.emailPassword(email, password);
        a.loginAsync(creds, t -> {
            if (t.isSuccess()) {
                user = a.currentUser();
                client = user.getMongoClient("mongodb-atlas");
                String id = user.getId();
                db = client.getDatabase("SoundVIE");
                pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
                MongoCollection<com.example.soundvieproject.model.User> usr = db.getCollection("user", com.example.soundvieproject.model.User.class).withCodecRegistry(pojoCodecRegistry);
                Document query = new Document("idUser", id);
                Document newVal = new Document("$set", new Document("email", email));
                Document newPhone = new Document("$set", new Document("phone", phone));
                Document newName = new Document("$set", new Document("name", name));
                usr.updateOne(query, newVal).getAsync(tsk -> {
                    if (tsk.isSuccess()) {
                        Log.d("Success", "succesfully inserted user");
                    } else {
                        Log.e("Error", "Error: " + tsk.getError().getErrorCode().toString());
                    }
                });
                usr.updateOne(query, newName).getAsync(tsk -> {
                    if (tsk.isSuccess()) {
                        Log.d("Success", "succesfully inserted user");
                    } else {
                        Log.e("Error", "Error: " + tsk.getError().getErrorCode().toString());
                    }
                });
                usr.updateOne(query, newPhone).getAsync(tsk -> {
                    if (tsk.isSuccess()) {
                        Log.d("Success", "succesfully inserted user");
                    } else {
                        Log.e("Error", "Error: " + tsk.getError().getErrorCode().toString());
                    }
                });
            } else {
                Log.e("Error", "Error: " + t.getError().getErrorCode().toString());
            }
        });
    }

    public void updateUser(String email, String phone, String name, String img, String desc, App.Callback<UpdateResult> callback) {
        user = a.currentUser();
        client = user.getMongoClient("mongodb-atlas");
        String id = user.getId();
        db = client.getDatabase("SoundVIE");
        pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoCollection<com.example.soundvieproject.model.User> usr = db.getCollection("user", com.example.soundvieproject.model.User.class).withCodecRegistry(pojoCodecRegistry);
        Document query = new Document("idUser", id);
        Document newVal = new Document("$set", new Document("email", email));
        Document newPhone = new Document("$set", new Document("phone", phone));
        Document newName = new Document("$set", new Document("name", name));
        Document newDesc = new Document("$set", new Document("moTa", desc));
        Document newAvatar = new Document("$set", new Document("avatar", img));
        usr.updateOne(query, newVal).getAsync(tsk -> {
            if (tsk.isSuccess()) {
                Log.d("Success", "succesfully inserted user");
            } else {
                Log.e("Error", "Error: " + tsk.getError().getErrorCode().toString());
            }
        });
        usr.updateOne(query, newName).getAsync(tsk -> {
            if (tsk.isSuccess()) {
                Log.d("Success", "succesfully inserted user");
            } else {
                Log.e("Error", "Error: " + tsk.getError().getErrorCode().toString());
            }
        });
        usr.updateOne(query, newPhone).getAsync(tsk -> {
            if (tsk.isSuccess()) {
                Log.d("Success", "succesfully inserted user");
            } else {
                Log.e("Error", "Error: " + tsk.getError().getErrorCode().toString());
            }
        });
        usr.updateOne(query, newDesc).getAsync(tsk -> {
            if (tsk.isSuccess()) {
                Log.d("Success", "succesfully inserted user");
            } else {
                Log.e("Error", "Error: " + tsk.getError().getErrorCode().toString());
            }
        });
        usr.updateOne(query, newAvatar).getAsync(callback);
    }

    public void logOut(App.Callback<User> callback) {
        user.logOutAsync(callback);
    }

    public MongoClient getClient() {
        return client;
    }

    public void setClient(MongoClient client) {
        this.client = client;
    }

    public MongoDatabase getDb() {
        return db;
    }

    public void setDb(MongoDatabase db) {
        this.db = db;
    }

    public CodecRegistry getPojoCodecRegistry() {
        return pojoCodecRegistry;
    }

    public void setPojoCodecRegistry(CodecRegistry pojoCodecRegistry) {
        this.pojoCodecRegistry = pojoCodecRegistry;
    }

    public void checkRole(App.Callback<com.example.soundvieproject.model.User> callback) {
        getUserCurrentBy(callback);
    }

    public void getUserByObjID(String id, App.Callback<com.example.soundvieproject.model.User> callback) {
        Document doc = new Document("idUser", id);
        MongoCollection<com.example.soundvieproject.model.User> col = db.getCollection("user", com.example.soundvieproject.model.User.class).withCodecRegistry(pojoCodecRegistry);
        col.findOne(doc).getAsync(callback);
    }

    public void getPlayistByID(App.Callback<MongoCursor<Playlist>> callback) {
        String id = Objects.requireNonNull(a.currentUser()).getId().toString();
        Document doc = new Document("idUser", id);
        MongoCollection<Playlist> playlists = db.getCollection("Playlist", Playlist.class).withCodecRegistry(pojoCodecRegistry);
        RealmResultTask<MongoCursor<Playlist>> tsk = playlists.find(doc).iterator();
        tsk.getAsync(callback);
    }

    public void getSongInPlayList(App.Callback<MongoCursor<SongInPlayList>> callback, String idPlayListCurrent){
        Document docu = new Document("idPlaylist", new ObjectId(idPlayListCurrent));
        MongoCollection<SongInPlayList> colSongInPl = db.getCollection("SongInPlaylist", SongInPlayList.class).withCodecRegistry(pojoCodecRegistry);
        RealmResultTask<MongoCursor<SongInPlayList>> task = colSongInPl.find(docu).iterator();
        task.getAsync(callback);
    }
    public void getSongByQuery(App.Callback<MongoCursor<Song>> callback,String name){
        Document doc = new Document();
        doc.append("$regex", "(?)"+Pattern.quote(name));
        doc.append("$options", "i");
        Document find = new Document();
        find.append("nameSong",doc);
        MongoCollection<Song> col = db.getCollection("Song", Song.class).withCodecRegistry(pojoCodecRegistry);
        RealmResultTask<MongoCursor<Song>> tsk = col.find(find).iterator();
        tsk.getAsync(callback);
    }
    public void getArtistByQuery(App.Callback<MongoCursor<com.example.soundvieproject.model.User>> callback, String name){
        Document doc = new Document();
        doc.append("$regex", "(?)"+Pattern.quote(name));
        doc.append("$options", "i");
        Document find = new Document();
        find.append("name",doc);
        find.append("idLoai", "ns");
        MongoCollection<com.example.soundvieproject.model.User> col = db.getCollection("user", com.example.soundvieproject.model.User.class).withCodecRegistry(pojoCodecRegistry);
        RealmResultTask<MongoCursor<com.example.soundvieproject.model.User>> tsk = col.find(find).iterator();
        tsk.getAsync(callback);
    }

}
