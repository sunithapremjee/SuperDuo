package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class TodayWidgetRemoteViewsService extends RemoteViewsService {

    public static String LOG_TAG = "TodayWidgetRemoteViewsService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

       // Log.d(LOG_TAG, "onGetViewFactory");
        return new FootballWidgetFactory(getApplicationContext(), intent);

    }


    class FootballWidgetFactory implements RemoteViewsFactory {

        Cursor mCursor;

        private Context mContext;

        public FootballWidgetFactory(Context context, Intent intent) {

            mContext = context;

        }

        @Override
        public void onCreate() {

            mCursor = getContentResolver().query(DatabaseContract.BASE_CONTENT_URI, null, null, null, null);

        }

        @Override
        public void onDataSetChanged() {

            mCursor = getContentResolver().query(DatabaseContract.BASE_CONTENT_URI,null,null,null,null);

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int i) {

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_today_item);

            if( mCursor.moveToPosition(i) )
            {
                String home_name = mCursor.getString( scoresAdapter.COL_HOME );
                String away_name = mCursor.getString( scoresAdapter.COL_AWAY );
                String match_time = mCursor.getString( scoresAdapter.COL_MATCHTIME );
                String score = Utilities.getScores(mCursor.getInt(scoresAdapter.COL_HOME_GOALS),
                        mCursor.getInt(scoresAdapter.COL_AWAY_GOALS));


                remoteViews.setTextViewText(R.id.football_widget_game_name,home_name + "  " + mContext.getString( R.string.vs ) + "  "+ away_name);
                remoteViews.setContentDescription(R.id.football_widget_game_name, home_name + mContext.getString( R.string.vs ) + away_name);
                remoteViews.setTextViewText(R.id.football_widget_match_time,  mContext.getString(R.string.time) + match_time);
                remoteViews.setContentDescription(R.id.football_widget_match_time,  mContext.getString(R.string.time) + match_time);
                remoteViews.setTextViewText(R.id.football_widget_score,  mContext.getString(R.string.score) + score);
                remoteViews.setContentDescription(R.id.football_widget_score,  mContext.getString(R.string.score) + score);
            }

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {

            if( mCursor.moveToPosition(i) ){

                return mCursor.getLong( scoresAdapter.COL_ID );
            }
            return 0;

        }

        @Override
        public boolean hasStableIds() {

            return false;
        }
    }
}