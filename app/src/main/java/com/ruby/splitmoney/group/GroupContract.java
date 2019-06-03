package com.ruby.splitmoney.group;

import com.ruby.splitmoney.objects.Group;
import com.ruby.splitmoney.util.BasePresenter;
import com.ruby.splitmoney.util.BaseView;

import java.util.List;

public interface GroupContract {

    interface View extends BaseView<Presenter> {

        void setGroupList(List<Group> groups);

        void setGroupDetailPage(String groupId);

    }

    interface Presenter extends BasePresenter {

        void transToGroupDetailPage(String groupId);

        void deleteGroupDialog(String groupId);
    }
}
