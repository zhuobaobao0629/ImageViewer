package indi.liyi.example.ui;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.huawei.hms.mlsdk.common.LensEngine;
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzer;
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzerFactory;
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzerSetting;
import com.liyi.grid.AutoGridView;
import com.liyi.grid.adapter.SimpleAutoGridAdapter;

import indi.liyi.example.R;
import indi.liyi.example.utils.PhotoLoader;
import indi.liyi.example.utils.SourceUtil;
import indi.liyi.example.utils.glide.GlideUtil;
import indi.liyi.viewer.ImageViewer;
import indi.liyi.viewer.ViewerStatus;
import indi.liyi.viewer.listener.OnBrowseStatusListener;

/**
 * 简单的预览界面
 */
public class SimplePrevActivity extends BaseActivity {
    private ImageViewer imageViewer;
    private AutoGridView autoGv;
    private SimpleAutoGridAdapter adapter;

//    private static MLHandKeypointAnalyzer analyzer;
    private static LensEngine lensEngine;
    static int width = 0;
    static int height = 0;


    MLHandKeypointAnalyzerSetting setting = new MLHandKeypointAnalyzerSetting.Factory()
            // MLHandKeypointAnalyzerSetting.TYPE_ALL表示所有结果都返回。
            // MLHandKeypointAnalyzerSetting.TYPE_KEYPOINT_ONLY表示只返回手部关键点信息。
            // MLHandKeypointAnalyzerSetting.TYPE_RECT_ONLY表示只返回手掌区域信息。
            .setSceneType(MLHandKeypointAnalyzerSetting.TYPE_ALL)
            // 设置同一张图片中最多支持检测的手部区域个数。默认最多支持10个手部区域信息检测。
            .setMaxHandResults(1)
            .create();

    MLHandKeypointAnalyzer analyzer = MLHandKeypointAnalyzerFactory.getInstance().getHandKeypointAnalyzer(setting);
    analyzer.setTransactor(new HandKeypointTransactor());

    @Override
    public int getLayoutId() {
        return R.layout.activity_simple_prev;
    }

    @Override
    public void initView() {
        autoGv = findViewById(R.id.autoGridView);
        imageViewer = findViewById(R.id.imageViewer);

        imageViewer.overlayStatusBar(false)
                .imageData(SourceUtil.getImageList())
                .imageLoader(new PhotoLoader());

        adapter = new SimpleAutoGridAdapter();
        adapter.setSource(SourceUtil.getImageList());
        adapter.setImageLoader(new SimpleAutoGridAdapter.ImageLoader() {
            @Override
            public void onLoadImage(final int position, Object source, final ImageView imageView, int viewType) {
                GlideUtil.loadImage(SimplePrevActivity.this, source, new SimpleTarget<Drawable>() {

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageViewer.getViewData().get(position).setImageWidth(resource.getIntrinsicWidth());
                        imageViewer.getViewData().get(position).setImageHeight(resource.getIntrinsicHeight());
                        imageView.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        imageView.setImageDrawable(errorDrawable);
                    }
                });
            }
        });
    }

    @Override
    public void addListener() {
        autoGv.setOnItemClickListener(new AutoGridView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                imageViewer.bindViewGroup(autoGv);
                imageViewer.watch(position);
            }
        });
        autoGv.setAdapter(adapter);
        imageViewer.setOnBrowseStatusListener(new OnBrowseStatusListener() {
            @Override
            public void onBrowseStatus(int status) {
                if (status == ViewerStatus.STATUS_BEGIN_OPEN) {
                    changeStatusBarColor(R.color.colorBlack);
                } else if (status == ViewerStatus.STATUS_SILENCE) {
                    changeStatusBarColor(R.color.colorPrimaryDark);
                }
            }
        });
    }

    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean b = imageViewer.onKeyDown(keyCode, event);
        if (b) {
            return b;
        }
        return super.onKeyDown(keyCode, event);
    }
}
