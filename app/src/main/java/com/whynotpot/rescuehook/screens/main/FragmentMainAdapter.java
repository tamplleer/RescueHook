package com.whynotpot.rescuehook.screens.main;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.whynotpot.rescuehook.screens.overScreen.OverScreenFragment;
import com.whynotpot.rescuehook.themes.Theme;
import com.whynotpot.rescuehook.themes.ThemeSimpleAlpha;
import com.whynotpot.rescuehook.themes.ThemeSimpleAlphaPicture;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import timber.log.Timber;

public class FragmentMainAdapter extends FragmentStateAdapter {

    private List<FragmentCardTheme> themes;
    private LayoutInflater inflater;
    private FragmentManager fragmentManager;
    private List<String> themesId;

    public FragmentMainAdapter(@NonNull FragmentActivity fragmentActivity, LayoutInflater inflater, FragmentManager fragmentManager) {
        super(fragmentActivity);
        this.inflater = inflater;
        this.fragmentManager = fragmentManager;
        this.themes = createThemes();


    }

    private List<FragmentCardTheme> createThemes() {
        List<FragmentCardTheme> themes = new ArrayList<>();
        themesId = new ArrayList<>();
        themesId.add("alpha_text");
        themesId.add("alpha_pic");
        themes.add(new FragmentCardTheme(new ThemeSimpleAlpha()));
        themes.add(new FragmentCardTheme(new ThemeSimpleAlphaPicture()));
        return themes;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return themes.get(position);
    }

    public void clear() {
        themes.clear();
    }

    @Override
    public int getItemCount() {
        return themes.size();
    }

    public String getThemeId(int page) {
        int id = (int) getItemId(page);
        if (id >= 0 && id < getItemCount()) {
            Timber.i(id + "");
            return themesId.get(id);
        }
        return themesId.get(0);
    }
}
