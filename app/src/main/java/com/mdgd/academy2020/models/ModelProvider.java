package com.mdgd.academy2020.models;

import com.mdgd.academy2020.models.cache.Cache;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.prefs.Prefs;

public interface ModelProvider {

    Cache getCache();

    Network getNetwork();

    Prefs getPrefs();
}
