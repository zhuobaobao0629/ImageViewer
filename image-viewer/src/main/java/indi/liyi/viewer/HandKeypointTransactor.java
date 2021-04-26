package indi.liyi.viewer;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huawei.hms.mlsdk.common.MLAnalyzer;
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypoint;
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzer;
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzerFactory;
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzerSetting;
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypoints;

import java.util.List;

import indi.liyi.viewer.viewpager.ImagePagerAdapter;
import indi.liyi.viewer.viewpager.ImageViewPager;

import static com.huawei.hms.mlsdk.handkeypoint.MLHandKeypoint.TYPE_FOREFINGER_FOURTH;



public class HandKeypointTransactor implements MLAnalyzer.MLTransactor<MLHandKeypoints> {
    private ImageViewPager viewPager;
    private ImagePagerAdapter mAdapter;
    private List<ViewData> mSourceList;

    @Override
    public void transactResult(MLAnalyzer.Result<MLHandKeypoints> results) {
        SparseArray<MLHandKeypoints> analyseList = results.getAnalyseList();
        if (analyseList == null || analyseList.size() <= 0) {
            return;
        }
        MLHandKeypoint center=analyseList.get(0).getHandKeypoint(TYPE_FOREFINGER_FOURTH);

        Log.d("Gesture", "OKKKK");
//        mAdapter = new ImagePagerAdapter(mSourceList.size()) {
//            @NonNull
//            @Override
//            public Object instantiateItem(@NonNull ViewGroup container, int position) {
//                return ImageViewer.instantiateItem(container, position, startPosition, callback);
//            }
//        };
//        viewPager.setAdapter(mAdapter);
//        viewPager.setCurrentItem(startPosition);

    }

    @Override
    public void destroy() {

    }
}