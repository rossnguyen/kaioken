package agv.kaak.vn.kaioken.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by shakutara on 10/5/17.
 */

public class GridView_ShowAll extends GridView {

    public GridView_ShowAll(Context context) {
        super(context);
    }

    public GridView_ShowAll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridView_ShowAll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {

            // The two leftmost bits in the height measure spec have
            // drawable_gradient_black special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        } else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}