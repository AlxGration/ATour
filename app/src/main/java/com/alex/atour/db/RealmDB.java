package com.alex.atour.db;

import android.util.Log;

import com.alex.atour.DTO.Estimation;
import com.alex.atour.DTO.MemberEstimation;

import java.util.List;
import java.util.TreeSet;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmDB {

    private final RealmConfiguration config = new RealmConfiguration.Builder()
            .name("ATour.realm")
            .schemaVersion(1)
            .build();
    private final Realm realm = Realm.getInstance(config);

    private enum FLAGS {
        champID,
        refereeID,
        id
    }

//    public void writeEstimation(Estimation estim) {
//        realm.executeTransactionAsync(t->{
//            t.insertOrUpdate(estim);
//        });
//    }
//    public TreeSet<Estimation> getEstimations(String champID, String refereeID){
//        RealmResults<Estimation> results = realm.where(Estimation.class)
//                .equalTo(FLAGS.champID.name(), champID)
//                .and()
//                .equalTo(FLAGS.refereeID.name(), refereeID)
//                .findAllAsync();
//        return new TreeSet<>(results);
//    }

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
}
