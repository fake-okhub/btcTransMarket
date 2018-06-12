package com.android.bitglobal.dao;

import com.android.bitglobal.common.AppContext;
import com.android.bitglobal.entity.Article;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * xiezuofei
 * 2016-11-18 15:20
 * 793169940@qq.com
 */
public class ArticleDao {
    private static ArticleDao articleDao;
    public static synchronized ArticleDao getInstance() {
        if (articleDao == null) {
            articleDao = new ArticleDao();
        }
        return articleDao;
    }
    private Realm realm= AppContext.getRealm();
    public List<Article> getIfon(){
        List<Article> article=realm.where(Article.class).equalTo("isVisible", "1").findAll();
        return article;
    }
    public void add(Article article){
        int article_size=realm.where(Article.class)
                .equalTo("id", article.getId()).findAll().size();
        if(article_size==0){
            realm.beginTransaction();
            article.setIsVisible("1");
            realm.copyToRealm(article);//提交操作
            realm.commitTransaction();
        }

    }
    public void delect(Article article){
        realm.beginTransaction();
        article.setIsVisible("0");
        realm.copyToRealmOrUpdate(article);//提交操作
        realm.commitTransaction();
    }
    public void delect(){
        RealmResults<Article> realmResults = realm.where(Article.class).findAll();
        realm.beginTransaction();
        for (Article article:realmResults){
            article.setIsVisible("0");
        }
        realm.commitTransaction();
    }


}
