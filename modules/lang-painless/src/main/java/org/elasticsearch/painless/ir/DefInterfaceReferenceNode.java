/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.painless.ir;

import org.elasticsearch.painless.ClassWriter;
import org.elasticsearch.painless.Location;
import org.elasticsearch.painless.MethodWriter;
import org.elasticsearch.painless.phase.IRTreeVisitor;
import org.elasticsearch.painless.symbol.WriteScope;
import org.objectweb.asm.Opcodes;

public class DefInterfaceReferenceNode extends ReferenceNode {

    /* ---- begin node data ---- */

    private String defReferenceEncoding;

    public void setDefReferenceEncoding(String defReferenceEncoding) {
        this.defReferenceEncoding = defReferenceEncoding;
    }

    public String getDefReferenceEncoding() {
        return defReferenceEncoding;
    }

    /* ---- end node data, begin visitor ---- */

    @Override
    public <Scope> void visit(IRTreeVisitor<Scope> irTreeVisitor, Scope scope) {
        irTreeVisitor.visitDefInterfaceReference(this, scope);
    }

    @Override
    public <Scope> void visitChildren(IRTreeVisitor<Scope> irTreeVisitor, Scope scope) {
        // do nothing; terminal node
    }

    /* ---- end visitor ---- */

    public DefInterfaceReferenceNode(Location location) {
        super(location);
    }

    @Override
    protected void write(ClassWriter classWriter, MethodWriter methodWriter, WriteScope writeScope) {
        methodWriter.writeDebugInfo(getLocation());

        // place holder for functional interface receiver
        // which is resolved and replace at runtime
        methodWriter.push((String)null);

        for (String capture : getCaptures()) {
            WriteScope.Variable variable = writeScope.getVariable(capture);
            methodWriter.visitVarInsn(variable.getAsmType().getOpcode(Opcodes.ILOAD), variable.getSlot());
        }
    }
}