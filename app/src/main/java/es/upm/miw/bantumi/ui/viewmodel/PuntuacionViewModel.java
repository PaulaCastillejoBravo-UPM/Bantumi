
package es.upm.miw.bantumi.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PuntuacionViewModel extends ViewModel {

    private MutableLiveData<String> fechaSeleccionada;

    public PuntuacionViewModel() {
        fechaSeleccionada = new MutableLiveData<>(null);
    }

    @NonNull
    public LiveData<String> getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public void setFechaSeleccionada(String fechaSeleccionada) {
        this.fechaSeleccionada.setValue(fechaSeleccionada);
    }
}
