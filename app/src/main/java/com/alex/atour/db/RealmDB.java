package com.alex.atour.db;

import android.util.Log;

import com.alex.atour.DTO.Estimation;
import com.alex.atour.DTO.MemberEstimation;
import com.alex.atour.DTO._Estimation;
import com.alex.atour.models.IEstimation;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class RealmDB {

    private final RealmConfiguration config = new RealmConfiguration.Builder()
            .name("ATour.realm")
            .schemaVersion(1)
            .build();
    private final Realm realm = Realm.getInstance(config);

    private enum FLAGS {
        champID,
        refereeID,
        memberID,
        id
    }

    public void writeEstimations(List<Estimation> mEstims){
        List<_Estimation> list = new ArrayList<>(mEstims.size());
        for(Estimation m: mEstims){list.add(new _Estimation(m));}

        realm.executeTransactionAsync(t->{
            t.insertOrUpdate(list);
        });
    }
    public TreeSet<Estimation> getEstimations(String refereeID){
        RealmResults<_Estimation> results = realm.where(_Estimation.class)
                .equalTo(FLAGS.refereeID.name(), refereeID)
                .findAllAsync();

        TreeSet<_Estimation> tree =  new TreeSet<>(results);
        TreeSet<Estimation> list = new TreeSet<>();
        for(_Estimation m: tree){list.add(m.getEstimationObject());}
        return list;
    }

    public void writeMemberEstimations(List<MemberEstimation> mEstims){
        realm.executeTransactionAsync(t->{
            t.insertOrUpdate(mEstims);
            Log.e("TAG", "realm estims list saved");
        });
    }
    public TreeSet<MemberEstimation> getMemberEstimations(String champID, String refereeID){
        RealmResults<MemberEstimation> results = realm.where(MemberEstimation.class)
                .equalTo(FLAGS.champID.name(), champID)
                .and()
                .equalTo(FLAGS.refereeID.name(), refereeID)
                .findAllAsync();
        return new TreeSet<>(results);
    }

    public void writeMemberEstimation(MemberEstimation mEstim){
        realm.executeTransactionAsync(t->{
            t.insertOrUpdate(mEstim);
            Log.e("TAG", "realm estim saved");
        });
    }

    public MemberEstimation getMemberEstimationByID(String id) {
        return realm.where(MemberEstimation.class)
                .equalTo(FLAGS.id.name(), id)
                .findFirst();
    }

    public ArrayList<_Estimation> getEstimationsOfUser(String champID, String userID){
        RealmResults<_Estimation> results = realm.where(_Estimation.class)
                .equalTo(FLAGS.champID.name(), champID)
                .and()
                .equalTo(FLAGS.memberID.name(), userID)
                .findAllAsync();
        return new ArrayList<>(results);
    }

    public String[] getAllMembersIDs(String champID){
        RealmResults<_Estimation> results = realm.where(_Estimation.class)
                .equalTo(FLAGS.champID.name(), champID)
                .findAllAsync();
        String[] membersIDs = new String[results.size()];
        for (int i = 0; i < results.size(); i++){
            membersIDs[i] = results.get(i).getMemberID();
        }
        return membersIDs;
    }
}
