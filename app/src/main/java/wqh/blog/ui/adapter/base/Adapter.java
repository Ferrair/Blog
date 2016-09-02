package wqh.blog.ui.adapter.base;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by WQH on 2016/4/11  21:21.
 * <p>
 * Interface for all <code>Adapter<code/>.
 * subclass MUST have a <code>List<Model><code/> stores the data.
 */
public interface Adapter<Model> {

    boolean isEmpty();

    /**
     * Fill the Adapter by the given newDataList.
     * Means clear the last data.
     */
    void fill(@NonNull List<Model> newDataList);

    /**
     * Update the Adapter by given newData.
     * Only update the place where to update.
     */
    void update(@NonNull Model newData);

    /**
     * Add a newData.
     */
    void addOne(@NonNull Model data, int position);

    void addAtTail(@NonNull Model data);

    void addAtHead(@NonNull Model data);

    void removeOne(@NonNull Model item);

    void removeOne(int position);

    void removeAll();

    List<Model> getAllData();

    Model getOne(int which);
}
