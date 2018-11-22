package com.mammen.generator.wrappers;

import com.mammen.generator.DriveBase;
import com.mammen.generator.Units;
import com.mammen.util.Mathf;
import jaci.pathfinder.Trajectory;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PfV1GeneratorVars implements GeneratorVars
{

    public enum FitMethod
    {
        HERMITE_CUBIC( "HERMITE_CUBIC", "Cubic", Trajectory.FitMethod.HERMITE_CUBIC ),
        HERMITE_QUINTIC( "HERMITE_QUINTIC", "Quintic", Trajectory.FitMethod.HERMITE_QUINTIC );

        private String label;
        private String internalLabel;
        private jaci.pathfinder.Trajectory.FitMethod pf_fitMethod;

        FitMethod( String internalLabel, String label, Trajectory.FitMethod fitMethod )
        {
            this.internalLabel = internalLabel;
            this.label = label;
            this.pf_fitMethod = fitMethod;
        }

        @Override
        public String toString()
        {
            return label;
        }

        public String getInternalLabel()
        {
            return internalLabel;
        }

        public Trajectory.FitMethod pfFitMethod()
        {
            return pf_fitMethod;
        }
    }

    private DoubleProperty timeStep         = new SimpleDoubleProperty( 0.05 );
    private DoubleProperty velocity         = new SimpleDoubleProperty( 4.0 );
    private DoubleProperty accel            = new SimpleDoubleProperty( 3.0 );
    private DoubleProperty jerk             = new SimpleDoubleProperty( 5.0 );
    private DoubleProperty wheelBaseW       = new SimpleDoubleProperty( 1.5 );
    private DoubleProperty wheelBaseD       = new SimpleDoubleProperty( 2.0 );
    private Property<FitMethod> fitMethod   = new SimpleObjectProperty<>( FitMethod.HERMITE_CUBIC );
    private Property<DriveBase> driveBase   = new SimpleObjectProperty<>( DriveBase.TANK );
    private Property<Units> unit           = new SimpleObjectProperty<>( Units.FEET );


    /**
     * Resets configuration to default values for the given unit.
     */
    public void setDefaultValues( Units newUnit )
    {
        fitMethod.setValue( FitMethod.HERMITE_CUBIC );
        driveBase.setValue( DriveBase.TANK );
        unit.setValue( newUnit );

        switch( newUnit )
        {
            case FEET:
                timeStep.set( 0.05 );
                velocity.set( 4.0 );
                accel.set( 3.0 );
                jerk.set( 5.0 );
                wheelBaseW.set( 1.5 );
                wheelBaseD.set( 2.0 );
                break;

            case METERS:
                timeStep.set( 0.05 );
                velocity.set( 1.2192 );
                accel.set( 0.9144 );
                jerk.set( 18.288 );
                wheelBaseW.set( 0.4462272 );
                wheelBaseD.set( 0.4462272 );
                break;

            case INCHES:
                timeStep.set( 0.05 );
                velocity.set( 48 );
                accel.set( 36 );
                jerk .set( 720 );
                wheelBaseW.set( 17.568 );
                wheelBaseD.set( 17.568 );
                break;
        }
    }

    public void changeUnit( Units newUnit )
    {
        if( newUnit == unit.getValue() )
            return;

        // Convert each MP variable to the new unit
        double tmp_WBW = 0, tmp_WBD = 0, tmp_vel = 0, tmp_acc = 0, tmp_jer = 0;

        // convert to intermediate unit of feet
        switch( unit.getValue() )
        {
            case FEET:
                tmp_WBW = wheelBaseW.get();
                tmp_WBD = wheelBaseD.get();
                tmp_vel = velocity.get();
                tmp_acc = accel.get();
                tmp_jer = jerk.get();
                break;

            case INCHES:
                tmp_WBW = Mathf.inchesToFeet( wheelBaseW.get() );
                tmp_WBD = Mathf.inchesToFeet( wheelBaseD.get() );
                tmp_vel = Mathf.inchesToFeet( velocity.get() );
                tmp_acc = Mathf.inchesToFeet( accel.get() );
                tmp_jer = Mathf.inchesToFeet( jerk.get() );
                break;

            case METERS:
                tmp_WBW = Mathf.meterToFeet( wheelBaseW.get() );
                tmp_WBD = Mathf.meterToFeet( wheelBaseD.get() );
                tmp_vel = Mathf.meterToFeet( velocity.get() );
                tmp_acc = Mathf.meterToFeet( accel.get() );
                tmp_jer = Mathf.meterToFeet( jerk.get() );
                break;
        }

        // convert from intermediate unit of feet
        switch( newUnit )
        {
            case FEET:
                wheelBaseW  .set( tmp_WBW );
                wheelBaseD  .set( tmp_WBD );
                velocity    .set( tmp_vel );
                accel       .set( tmp_acc );
                jerk        .set( tmp_jer );
                break;

            case INCHES:
                wheelBaseW  .set( Mathf.round( Mathf.feetToInches( tmp_WBW ),4 ) );
                wheelBaseD  .set( Mathf.round( Mathf.feetToInches( tmp_WBD ),4 ) );
                velocity    .set( Mathf.round( Mathf.feetToInches( tmp_vel ),4 ) );
                accel       .set( Mathf.round( Mathf.feetToInches( tmp_acc ),4 ) );
                jerk        .set( Mathf.round( Mathf.feetToInches( tmp_jer ),4 ) );

                break;

            case METERS:
                wheelBaseW  .set( Mathf.round( Mathf.feetToMeter( tmp_WBW ),4 ) );
                wheelBaseD  .set( Mathf.round( Mathf.feetToMeter( tmp_WBD ),4 ) );
                velocity    .set( Mathf.round( Mathf.feetToMeter( tmp_vel ),4 ) );
                accel       .set( Mathf.round( Mathf.feetToMeter( tmp_acc ),4 ) );
                jerk        .set( Mathf.round( Mathf.feetToMeter( tmp_jer ),4 ) );
                break;
        }
    }

    // Getters and Setters
    public double getTimeStep()
    {
        return timeStep.get();
    }

    public DoubleProperty timeStepProperty()
    {
        return timeStep;
    }

    public void setTimeStep( double timeStep )
    {
        this.timeStep.set( timeStep );
    }

    public double getVelocity()
    {
        return velocity.get();
    }

    public DoubleProperty velocityProperty()
    {
        return velocity;
    }

    public void setVelocity( double velocity )
    {
        this.velocity.set( velocity );
    }

    public double getAccel()
    {
        return accel.get();
    }

    public DoubleProperty accelProperty()
    {
        return accel;
    }

    public void setAccel( double accel )
    {
        this.accel.set( accel );
    }

    public double getJerk()
    {
        return jerk.get();
    }

    public DoubleProperty jerkProperty()
    {
        return jerk;
    }

    public void setJerk( double jerk )
    {
        this.jerk.set( jerk );
    }

    public double getWheelBaseW()
    {
        return wheelBaseW.get();
    }

    public DoubleProperty wheelBaseWProperty()
    {
        return wheelBaseW;
    }

    public void setWheelBaseW( double wheelBaseW )
    {
        this.wheelBaseW.set( wheelBaseW );
    }

    public double getWheelBaseD()
    {
        return wheelBaseD.get();
    }

    public DoubleProperty wheelBaseDProperty()
    {
        return wheelBaseD;
    }

    public void setWheelBaseD( double wheelBaseD )
    {
        this.wheelBaseD.set( wheelBaseD );
    }

    public DriveBase getDriveBase()
    {
        return driveBase.getValue();
    }

    public Property<DriveBase> driveBaseProperty()
    {
        return driveBase;
    }

    public void setDriveBase( DriveBase driveBase )
    {
        this.driveBase.setValue( driveBase );
    }

    public FitMethod getFitMethod()
    {
        return fitMethod.getValue();
    }

    public Property<FitMethod> fitMethodProperty()
    {
        return fitMethod;
    }

    public void setFitMethod( FitMethod fitMethod )
    {
        this.fitMethod.setValue( fitMethod );
    }

    public Units getUnit()
    {
        return unit.getValue();
    }

    public Property<Units> unitProperty()
    {
        return unit;
    }

    public void setUnit( Units unit )
    {
        this.unit.setValue( unit );
    }
}