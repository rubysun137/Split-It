package com.ruby.splitmoney.quicksplit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class QuickSplitPresenterTest {
    QuickSplitPresenter mQuickSplitPresenter;
    QuickSplitFragment mQuickSplitFragment;

    @Before
    public void setUp() throws Exception {
        mQuickSplitFragment = Mockito.mock(QuickSplitFragment.class);
        mQuickSplitPresenter = new QuickSplitPresenter(mQuickSplitFragment);

    }

    @Test
    public void isSharedListEmpty() {
        mQuickSplitPresenter.setListSize(3);
//        mQuickSplitPresenter.addSharedMoneyList(0, 1);
//        mQuickSplitPresenter.addSharedMoneyList(1, 2);
//        mQuickSplitPresenter.addSharedMoneyList(2, 3);
//        Assert.assertFalse(mQuickSplitPresenter.isSharedListEmpty());
        Assert.assertTrue(mQuickSplitPresenter.isSharedListEmpty());
    }

    @Test
    public void toSecondPage() {
        mQuickSplitPresenter.selectSplitType(1);
        mQuickSplitPresenter.toSecondPage();
        Mockito.verify(mQuickSplitFragment, Mockito.times(1)).showSecondPage();
        Mockito.verify(mQuickSplitFragment, Mockito.times(1)).showEqualResult(0.0);
    }
}