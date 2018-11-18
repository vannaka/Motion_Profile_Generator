package com.mammen.generator.wrappers;

import com.mammen.generator.DriveBase;
import com.mammen.generator.Units;
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
    private Property<Units> units           = new SimpleObjectProperty<>( Units.FEET );


    /**
     * Resets configuration to default values for the given unit.
     */
    public void setDefaultValues( Units newUnits )
    {
        fitMethod.setValue( FitMethod.HERMITE_CUBIC );
        driveBase.setValue( DriveBase.TANK );
        units.setValue( newUnits );

        switch( newUnits )
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

    public Units getUnits()
    {
        return units.getValue();
    }

    public Property<Units> unitsProperty()
    {
        return units;
    }

    public void setUnits( Units units )
    {
        this.units.setValue(units);
    }
}