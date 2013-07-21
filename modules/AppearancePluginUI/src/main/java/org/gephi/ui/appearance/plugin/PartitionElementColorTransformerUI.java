/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.appearance.plugin;

import javax.swing.Icon;
import javax.swing.JPanel;
import org.gephi.appearance.api.Partition;
import org.gephi.appearance.plugin.PartitionElementColorTransformer;
import org.gephi.appearance.spi.Category;
import org.gephi.appearance.spi.PartitionTransformer;
import org.gephi.appearance.spi.PartitionTransformerUI;
import org.openide.util.NbBundle;

/**
 *
 * @author mbastian
 */
public class PartitionElementColorTransformerUI implements PartitionTransformerUI {

    private final PartitionColorTransformerPanel panel;

    public PartitionElementColorTransformerUI() {
        panel = new PartitionColorTransformerPanel();
    }

    @Override
    public Category[] getCategories() {
        return new Category[]{Category.NODE_COLOR, Category.EDGE_COLOR};
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(UniqueElementColorTransformerUI.class, "Attribute.name");
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public JPanel getPanel(PartitionTransformer transformer, Partition partition) {
        panel.setup(transformer, partition);
        return panel;
    }

    @Override
    public Class<? extends PartitionTransformer> getTransformerClass() {
        return PartitionElementColorTransformer.class;
    }
}