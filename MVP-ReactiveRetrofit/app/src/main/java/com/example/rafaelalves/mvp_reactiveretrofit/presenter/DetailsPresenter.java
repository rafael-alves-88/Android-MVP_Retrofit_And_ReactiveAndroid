package com.example.rafaelalves.mvp_reactiveretrofit.presenter;

import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.widget.Toast;

import com.example.rafaelalves.mvp_reactiveretrofit.model.Pokemon;
import com.example.rafaelalves.mvp_reactiveretrofit.model.PokemonForm;
import com.example.rafaelalves.mvp_reactiveretrofit.model.Result;
import com.example.rafaelalves.mvp_reactiveretrofit.repository.api.PokemonAPI;
import com.example.rafaelalves.mvp_reactiveretrofit.view.ui.DetailsActivity;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailsPresenter {

    private DetailsActivity mView;
    private PokemonAPI mPokemon;

    public DetailsPresenter(DetailsActivity activity) {
        mView = activity;
        mPokemon = new PokemonAPI();
    }

    public void loadPokemonDetails(Bundle bundle) {
        Result pokemonFromList = (Result) bundle.getSerializable(MainPresenter.POKEMON_FROM_LIST);

        mView.showLoading();

        mPokemon.getPokemonAPI()
                .getPokemon(pokemonFromList.name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Pokemon>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Pokemon pokemon) {
                        mView.setPokemonDetails(pokemon);
                    }
                });

        mPokemon.getPokemonAPI()
                .getPokemonForm(pokemonFromList.name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PokemonForm>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setPokemonSpriteError();
                    }

                    @Override
                    public void onNext(PokemonForm pokemonForm) {
                        mView.setPokemonSprite(pokemonForm.sprites.frontDefault);
                    }
                });
    }
}